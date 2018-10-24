package com.jwcjlu.gateway.core.metric;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jwcjlu.gateway.core.node.ServerInfo;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public enum MetricService {
    INSTACE;


    private final ConcurrentMap<String,  ConcurrentMap<ServerInfo,Metric>> metrics;


    MetricService() {
        this.metrics = buildMap();
    }


    protected ConcurrentMap<String,  ConcurrentMap<ServerInfo,Metric>> buildMap() {
        Cache<String, ConcurrentMap<ServerInfo,Metric>> cache = CacheBuilder.newBuilder().build();
        return cache.asMap();
    }


    public Metric getMetric(String name,ServerInfo info) {
        return metrics.computeIfAbsent(name, (key) -> {
            return new ConcurrentHashMap<>();
        }).computeIfAbsent(info,(key)->{
            return new EwayMetric();
        });
    }

    public void clear(String name) {
        if( metrics.get(name)!=null)
            metrics.get(name).forEach((k,v)->{v.clear();});

    }

    public void clear() {
        metrics.clear();
    }

    public ConcurrentMap<String, ConcurrentMap<ServerInfo, Metric>> getMetrics() {
        return metrics;
    }
}
