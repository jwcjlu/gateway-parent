package com.jwcjlu.gateway.httpServer.filter.support.counter;

import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.enums.ConcurrencyTypeEnum;
import com.jwcjlu.gateway.httpServer.redis.JedisService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ConcurrencyCounterFactory {
    INSTANCE;
    private static Map<String, ConcurrencyCounter> concurrencyCounterMap = new HashMap<>();
    private static ConcurrentMap<String, String> serviceKeyCounterTypeMap = new ConcurrentHashMap<>();

    static {
        concurrencyCounterMap.put(ConcurrencyTypeEnum.local.name(), new LocalConcurrencyCounter());
        concurrencyCounterMap.put(ConcurrencyTypeEnum.redis.name(), new RedisConcurrencyCounter(
            BootServiceManager.INSTANCE.findService(JedisService.class)));
    }

    public ConcurrencyCounter getConcurrencyCounter(String serviceKey) {
        String counterType = serviceKeyCounterTypeMap.getOrDefault(serviceKey, "");
        return concurrencyCounterMap.get(counterType);

    }

    public void register(String serviceKey, int counterType) {
        if (ConcurrencyTypeEnum.redis.getValue() == counterType) {
            serviceKeyCounterTypeMap.put(serviceKey, ConcurrencyTypeEnum.redis.name());
        }
        if (ConcurrencyTypeEnum.local.getValue() == counterType) {
            serviceKeyCounterTypeMap.put(serviceKey, ConcurrencyTypeEnum.local.name());
        }


    }

}
