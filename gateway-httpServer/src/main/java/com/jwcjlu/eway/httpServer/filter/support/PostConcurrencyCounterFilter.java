package com.jwcjlu.gateway.httpServer.filter.support;

import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.enums.CounterTypeEnum;
import com.jwcjlu.gateway.core.metric.Metric;
import com.jwcjlu.gateway.core.metric.MetricService;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.DTO.RequestDTO;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.filter.AbstractHttpFilter;
import com.jwcjlu.gateway.httpServer.filter.FilterChain;
import com.jwcjlu.gateway.httpServer.filter.FilterType;
import com.jwcjlu.gateway.httpServer.filter.support.counter.ConcurrencyCounter;
import com.jwcjlu.gateway.httpServer.filter.support.counter.ConcurrencyCounterFactory;
import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class PostConcurrencyCounterFilter extends AbstractHttpFilter {
    private Logger logger = LoggerFactory.getLogger(PostConcurrencyCounterFilter.class);

    @Override
    public String name() {
        return "postConcurrencyCounter";
    }

    @Override
    public int getOrder() {
        return 60;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.POST;
    }

    @Override
    public void doFilter(HttpHandlerContent content, FilterChain chain) throws Throwable {
        RequestDTO requestDTO = content.getAttribute(Constants.REQUESTDTO);
        counter(requestDTO, content);
        if (!content.getAttribute(Constants.CONCURRENCY_CONTROL, false)) {
            return;
        }
        ConcurrencyCounter concurrencyCounter = ConcurrencyCounterFactory.INSTANCE.getConcurrencyCounter(requestDTO.getServerKey());
        if (Objects.isNull(concurrencyCounter))
            return;
        concurrencyCounter.release(requestDTO.getServerKey(), 1);
    }

    public Class<? extends Handler> handlerClass() {
        return null;
    }

    private void counter(RequestDTO requestDTO, HttpHandlerContent content) {
        try {
            ServerInfo serverInfo = content.getAttribute(Constants.REAL_SERVER);
            if (Objects.isNull(serverInfo)) {
                return;
            }
            Metric metric = MetricService.INSTACE.getMetric(requestDTO.getServerKey(), serverInfo);
            if (Objects.isNull(content.getResponse()) || HttpUtils.INSTANCE.isBadResponse(content.getResponse())) {
                metric.inc(CounterTypeEnum.ERROR);
            } else {
                metric.inc(CounterTypeEnum.TOTAL);
            }
            Long startTime = content.getAttribute(Constants.START_TIME, new Long(0));
            if (startTime == 0) {
                return;
            }
            metric.inc(System.currentTimeMillis() - startTime, CounterTypeEnum.LOSTTIME);
        } catch (Throwable t) {
            logger.error("metric failure  ", t);
        }
    }

}
