package com.jwcjlu.gateway.httpServer.redis;
import com.jwcjlu.gateway.common.util.ConfigUtils;
import com.jwcjlu.gateway.httpServer.adapter.BootServiceAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class JedisService extends BootServiceAdapter {
    private JedisClusterWarp clusterWarp;

    @Override
    public void initialize(Properties properties) throws Exception {
        String hostAndPorts = ConfigUtils.getString(properties, "gateway.redis.address");
        String password = ConfigUtils.getString(properties, "gateway.redis.password");
        int timeout = ConfigUtils.getInt(properties, "gateway.redis.password");
        int maxRedirections = ConfigUtils.getInt(properties, "gateway.redis.maxRedirections");
        clusterWarp = new JedisClusterWarp(hostAndPorts, password, timeout, maxRedirections);
    }

    @Override
    public void shutdown() throws IOException {
        clusterWarp.shutdown();
    }

    public long incrBy(String key, long value) {
        return clusterWarp.incrBy(key, value);
    }

    public void decrBy(String key, long value) {
        clusterWarp.decrBy(key, value);
    }

    public Object eval(String script, List<String> keys, List<String> avgs) {
        return clusterWarp.eval(script, keys, avgs);
    }
}
