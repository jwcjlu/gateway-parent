package com.jwcjlu.gateway.core.lb;

import com.jwcjlu.gateway.core.lb.support.RandomLoadBalance;
import com.jwcjlu.gateway.core.lb.support.RoundRobinLoadBalance;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum LoadBalanceFactory {
    INSTANCE;

    static {
        new RandomLoadBalance();
        new RoundRobinLoadBalance();
    }

    private Map<String, LoadBalance> balances = new HashMap<String, LoadBalance>();

    public void registerLoadBalace(String name, LoadBalance loadBalance) {
        balances.put(name, loadBalance);
    }

    public LoadBalance getLoadBalance(String name) {
        if (balances.get(name) != null) {
            return balances.get(name);
        }
        return getDefaultLoadBalance();
    }

    public LoadBalance getLoadBalance(LoadBalanceEnum name) {
        if (Objects.isNull(name)) {
            return getDefaultLoadBalance();
        }
        return getLoadBalance(name.name());
    }

    public LoadBalance getDefaultLoadBalance() {
        return balances.get(LoadBalanceEnum.ROUNDROBIN.name());
    }
}
