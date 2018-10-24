package com.jwcjlu.gateway.httpServer.filter.support.counter;

public interface ConcurrencyCounter {

    /**
     * 释放
     *
     * @param value
     */
    void release(String key,int value);

    /**
     * @param capacity
     * @return
     */
    boolean isAllow(String key,int capacity,int value);
}
