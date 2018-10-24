package com.jwcjlu.gateway.core.lb.support;


import com.jwcjlu.gateway.core.lb.AbstractLoadBalance;
import com.jwcjlu.gateway.core.lb.LoadBalanceEnum;
import com.jwcjlu.gateway.core.node.ServerInfo;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    private Random random = new Random();

    @Override
    public ServerInfo doSelect(List<ServerInfo> servers) {
        int length = servers.size();
        int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        for (int i = 0; i < length; i++) {
            int weight = servers.get(i).getWeight();
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0
                && weight != servers.get(i - 1).getWeight()) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = random.nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < length; i++) {
                offset -= servers.get(i).getWeight();
                if (offset < 0) {
                    return servers.get(i);
                }
            }
        }
        return servers.get(random.nextInt(length));
    }

    public String name() {
        return LoadBalanceEnum.RANDOM.name();
    }
}
