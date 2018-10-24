package com.jwcjlu.gateway.core.lb.support;

import com.jwcjlu.gateway.core.lb.AbstractLoadBalance;
import com.jwcjlu.gateway.core.lb.LoadBalanceEnum;
import com.jwcjlu.gateway.core.node.ServerInfo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundRobinLoadBalance extends AbstractLoadBalance {
    private static AtomicInteger sequence = new AtomicInteger(1);

    private static final class WeightAndServerWrapper {

        private ServerInfo server;
        private int maxWeight;

        public WeightAndServerWrapper(ServerInfo server, int maxWeight) {
            this.server = server;
            this.maxWeight = maxWeight;
        }

        public int getWeight() {
            return server.getWeight();
        }

        public ServerInfo getServerInfo() {
            return server;
        }

        public int getMaxWeight() {
            return maxWeight;
        }

    }

    @Override
    protected ServerInfo doSelect(final List<ServerInfo> servers) {

        int length = servers.size(); // 总个数
        int maxWeight = 0; // 最大权重
        int minWeight = Integer.MAX_VALUE; // 最小权重
        final LinkedHashSet<WeightAndServerWrapper> weightAndServerSet = new LinkedHashSet<WeightAndServerWrapper>();
        int weightSum = 0;
        for (int i = 0; i < length; i++) {
            int weight = servers.get(i).getWeight();
            maxWeight = Math.max(maxWeight, weight); // 累计最大权重
            minWeight = Math.min(minWeight, weight); // 累计最小权重
            if (weight > 0) {
                weightSum += weight;
                weightAndServerSet.add(new WeightAndServerWrapper(servers.get(i), weightSum));

            }
        }
        int currentSequence = sequence.getAndIncrement();
        if (maxWeight > 0 && minWeight < maxWeight) { // 权重不一样
            int loopCount = currentSequence % (weightSum / minWeight) + 1;//
            for (WeightAndServerWrapper weightAndServer : weightAndServerSet) {
                int minValue = (weightAndServer.getMaxWeight() - weightAndServer.getWeight()) / minWeight;
                int maxValue = weightAndServer.getMaxWeight() / minWeight;
                if (maxValue >= loopCount && minValue < loopCount) {
                    return weightAndServer.getServerInfo();
                }
            }


        }
        return servers.get(currentSequence % length);
    }

    public String name() {
        return LoadBalanceEnum.ROUNDROBIN.name();
    }

}



