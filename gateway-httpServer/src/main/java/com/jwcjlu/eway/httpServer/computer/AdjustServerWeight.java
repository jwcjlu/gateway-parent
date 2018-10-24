package com.jwcjlu.gateway.httpServer.computer;

import com.jwcjlu.gateway.api.convert.DivideHandle;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.ArithmeticUtil;
import com.jwcjlu.gateway.core.metric.EwayMetric;
import com.jwcjlu.gateway.core.metric.Metric;
import com.jwcjlu.gateway.core.metric.MetricService;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.adapter.BootServiceAdapter;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class AdjustServerWeight extends BootServiceAdapter {
    private Logger logger = LoggerFactory.getLogger(AdjustServerWeight.class);
    private Timer timer;

    @Override
    public void onComplete() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ajustWeight();
            }
        }, 1000, 300 * 1000);
    }

    @Override
    public void shutdown() throws IOException {
        timer.cancel();
        timer = null;
    }

    private void ajustWeight() {
        ConcurrentMap<String, ConcurrentMap<ServerInfo, Metric>> metrics = MetricService.INSTACE.getMetrics();
        if (Objects.isNull(metrics)) {
            return;
        }
        metrics.forEach((serverKey, serverInfoMetric) -> {
            if (Objects.isNull(serverInfoMetric)) {
                MetricService.INSTACE.clear(serverKey);
                return;
            }
            if (serverInfoMetric.size() < 2) {
                MetricService.INSTACE.clear(serverKey);
                return;
            }
            computer(serverKey, serverInfoMetric);
        });

    }

    private void computer(String serverKey, ConcurrentMap<ServerInfo, Metric> metrics) {
        try {
            DivideHandle divide = BootServiceManager.INSTANCE.findService(DataCacheManager.class).getHandler("divide", serverKey);
            if (divide.getWeightType() == 1) {//权重不允许智能分配
                return;
            }
            if (CollectionUtils.isEmpty(divide.getUpstreamList())) {
                return;
            }
            final AtomicLong minLostTime = new AtomicLong(Long.MAX_VALUE);
            List<ServerMetric> serverMetricList = new ArrayList<>();
            metrics.forEach((serverInfo, metric) -> {
                EwayMetric em = (EwayMetric) metric;
                serverMetricList.add(new ServerMetric(serverInfo, em));
                minLostTime.set(ArithmeticUtil.min(minLostTime.get(), em.getLostTime().get()));
            });
            Double minRate = serverMetricList.stream().map(sm -> {
                return sm.calculate(minLostTime.get());
            }).min((o1, o2) -> {
                return (o1 - o2) > 0 ? 1 : -1;
            }).get();
            if (minRate <= 0) {
                return;
            }
            logger.info("serverKey [" + serverKey + "] weight  adjust before " + divide.getUpstreamList());
            divide.getUpstreamList().stream().map((upstream) -> {
                ServerMetric serverMetric = serverMetricList.stream().filter((sm) -> {
                    return sm.info.equals(new ServerInfo(upstream.getUpstreamHost(),
                        upstream.getUpstreamPort()));
                }).findFirst().get();
                if (Objects.isNull(serverMetric)) {
                    upstream.setWeight(100);
                } else {
                    upstream.setWeight((int) Math.round(serverMetric.calculate(minLostTime.get()) / minRate) * 100);
                }
                return upstream;
            }).collect(Collectors.toList());
            logger.info("serverKey [" + serverKey + "] weight  adjust after " + divide.getUpstreamList());
        } finally {
            MetricService.INSTACE.clear(serverKey);
        }
    }

    class ServerMetric {
        private ServerInfo info;
        private EwayMetric metric;

        public ServerMetric(ServerInfo info, EwayMetric metric) {
            this.info = info;
            this.metric = metric;
        }

        public Double calculate(long minLostTime) {
            return ((metric.getTotal().get() - metric.getError().get()) / ArithmeticUtil.max(metric.getTotal().get(), 1)) * (1 - Constants.LOSTTIME_RATE) +
                (minLostTime * metric.getTotal().get() / ArithmeticUtil.max(metric.getLostTime().get(), 1)) * Constants.LOSTTIME_RATE;
        }

    }

}
