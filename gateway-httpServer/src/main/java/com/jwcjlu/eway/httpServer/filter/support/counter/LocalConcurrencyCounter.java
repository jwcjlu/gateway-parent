package com.jwcjlu.gateway.httpServer.filter.support.counter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalConcurrencyCounter implements ConcurrencyCounter {
   // private Lock allowLock = new ReentrantLock();
    protected Long timeout = 120L; // 对应的超时时间,1分钟
    private ConcurrentMap<String, AtomicInteger> counter;
    private Logger logger = LoggerFactory.getLogger(LocalConcurrencyCounter.class);

    public LocalConcurrencyCounter() {
        Cache<String, AtomicInteger> cache = CacheBuilder.newBuilder().expireAfterAccess(timeout, TimeUnit.SECONDS).build();
        counter = cache.asMap();
    }

    @Override
    public synchronized void release(String key, int value) {
        System.out.println("----------------------");
        AtomicInteger count = counter.get(key);
        if (count == null || count.get() < 1) {
            return;
        }

           count.compareAndSet(count.get(), count.get() - value);

    }

    @Override
    public  synchronized  boolean isAllow(String key, int capacity, int value) {
        System.out.println("+++++++++++++++++++++");
            AtomicInteger count = counter.computeIfAbsent(key, s -> new AtomicInteger(0));
            boolean result = count.get() < capacity;
            if (result) {
                boolean flag = false;
                while (!flag) {
                    flag = count.compareAndSet(count.get(), count.get() + value);
                }
            }
            return result;

    }

}
