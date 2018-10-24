package com.jwcjlu.gateway.httpServer.filter.support.counter;

import com.jwcjlu.gateway.common.util.FileUtils;
import com.jwcjlu.gateway.httpServer.redis.JedisService;

import java.util.Arrays;
import java.util.List;

public class RedisConcurrencyCounter implements ConcurrencyCounter {
    private String script;
    private JedisService jedisService;

    public RedisConcurrencyCounter(JedisService jedisService) {
        this.jedisService = jedisService;
        script = FileUtils.readFile("META-INF/scripts/concurrency_control.lua");
    }

    @Override
    public void release(String key, int value) {
        jedisService.decrBy(key, value);
    }

    @Override
    public boolean isAllow(String key, int capacity, int value) {
        Object result= jedisService.eval(script, Arrays.asList(key), Arrays.asList(capacity + ""));
        List<Long> rs = null;
        if (result instanceof List)
            rs = (List<Long>) result;
        System.out.println(rs.get(1));
        System.out.println(rs.get(0));

        return rs == null ? false : rs.get(0) > 0;
    }

}
