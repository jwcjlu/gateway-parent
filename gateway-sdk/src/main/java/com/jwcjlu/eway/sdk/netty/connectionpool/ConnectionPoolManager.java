package com.jwcjlu.gateway.sdk.netty.connectionpool;

import com.jwcjlu.gateway.core.node.ServerInfo;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ConnectionPoolManager {
    INSTANCE;
    private ConcurrentMap<ServerInfo, ConnectionPool> connectionPools;
    private NioEventLoopGroup executors=new NioEventLoopGroup();

    private ConnectionPoolManager() {
        connectionPools = new ConcurrentHashMap<>(200);
    }

    public ConnectionPool getConnectionPool(ServerInfo server) {
        return getConnectionPool(server, null);
    }

    /**
     * 得到某个服务的连接池
     *
     * @param server
     * @param poolConfig
     * @return
     */
    public ConnectionPool getConnectionPool(ServerInfo server, GenericObjectPoolConfig poolConfig) {
        ConnectionPool connectionPool = connectionPools.computeIfAbsent(server, s -> {
                return new DefaultConnectionPool(server, poolConfig, executors);
            }

        );
        return connectionPool;
    }

    public void shutdown() {
        if (Objects.isNull(connectionPools)) {
            return;
        }
        connectionPools.values().forEach(conntion -> conntion.shutdown());
    }

    public void setExecutors(NioEventLoopGroup executors) {
        this.executors = executors;
    }

    public void romveServerInfo(ServerInfo info) {
        ConnectionPool pool = connectionPools.remove(info);
        if (pool != null) {
            pool.shutdown();
        }

    }


}
