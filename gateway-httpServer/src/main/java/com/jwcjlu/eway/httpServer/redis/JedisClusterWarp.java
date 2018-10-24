package com.jwcjlu.gateway.httpServer.redis;

import com.jwcjlu.gateway.common.util.StringUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class JedisClusterWarp {
    private JedisCluster jedisCluster;
    /**
     * ip端口识别正则
     */
    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    /**
     * 解析ip
     *
     * @return
     * @throws Exception
     */
    private Set<HostAndPort> parseHostAndPort(String host) throws Exception {
        try {
            String[] ipPorts = host.split(",");
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for (String key : ipPorts) {
                if (!"".equals(key)) {
                    boolean isIpPort = p.matcher(key).matches();
                    if (!isIpPort) {
                        throw new Exception("ip or port invaild");
                    }
                    String[] ipAndPort = key.split(":");

                    HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    haps.add(hap);
                }
            }
            return haps;
        } catch (Exception e) {
            throw new Exception("redis.properties  config error", e);
        }
    }

    public JedisClusterWarp(String host, String password, int timeout, int maxRedirections) throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort(host);
        if (StringUtil.isEmpty(password)) {
            jedisCluster = new JedisCluster(haps, timeout, maxRedirections, getDefaultGenericObjectPoolConfig());
        } else {
            jedisCluster = new JedisCluster(haps, timeout, timeout, maxRedirections, password, getDefaultGenericObjectPoolConfig());
        }

    }

    public static GenericObjectPoolConfig getDefaultGenericObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //连接满时最多等待时间
        config.setMaxWaitMillis(5000L);
        // 高级功能：
        //使用时检查对象（默认不检查）
        config.setTestWhileIdle(true);
        config.setTestOnCreate(true);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //jmx启用 之后可以实时的查看线程池对象的状态
        config.setJmxEnabled(false);
        config.setJmxNameBase("redisConnectionPool");
        config.setJmxNamePrefix("redisconnectionPool_");
        //驱逐线程每次检查对象个数
        config.setNumTestsPerEvictionRun(2);
        //空闲连接被驱逐前能够保留的时间
        config.setMinEvictableIdleTimeMillis(10000L);
        //当空闲线程大于minIdle 空闲连接能够保留时间，同时指定会被上面的覆盖
        config.setSoftMinEvictableIdleTimeMillis(600000L);
        //驱逐线程执行间隔时间
        config.setTimeBetweenEvictionRunsMillis(300000L);
        return config;
    }

    public void shutdown() throws IOException {
        jedisCluster.close();
    }

    public long incrBy(String key, long value) {
        return jedisCluster.incrBy(key, value);
    }

    public void decrBy(String key, long value) {
        jedisCluster.decrBy(key, value);
        jedisCluster.expire(key,120);

    }

    public Object eval(String script, List<String> keys, List<String> avgs) {
        return jedisCluster.eval(script, keys, avgs);
    }
    public Object evalSha(String script, List<String> keys, List<String> avgs){
        return jedisCluster.evalsha(script, keys, avgs);
    }

}
