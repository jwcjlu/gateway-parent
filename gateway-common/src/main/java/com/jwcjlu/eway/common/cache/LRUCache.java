package com.jwcjlu.gateway.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }
}
