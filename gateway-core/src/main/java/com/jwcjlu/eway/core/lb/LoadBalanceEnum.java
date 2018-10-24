package com.jwcjlu.gateway.core.lb;

public enum LoadBalanceEnum {
    RANDOM("random"),
    ROUNDROBIN("RoundRobin");
    private String name;

    private LoadBalanceEnum(String name) {
        this.name = name;
    }

}
