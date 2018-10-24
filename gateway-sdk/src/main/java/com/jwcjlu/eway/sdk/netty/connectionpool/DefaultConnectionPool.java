package com.jwcjlu.gateway.sdk.netty.connectionpool;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.netty.Connection;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Objects;

public class DefaultConnectionPool implements ConnectionPool {
    private final ServerInfo origin;
    private GenericObjectPool<Connection> connectionPool;
    private GenericObjectPoolConfig poolConfig;
    private BasePooledObjectFactory<Connection> factory;

    public DefaultConnectionPool(ServerInfo server, GenericObjectPoolConfig poolConfig, NioEventLoopGroup eventExecutors) {
        this.origin = server;
        this.factory = new ConnectionFactory(server, this, eventExecutors);
        if (Objects.isNull(poolConfig)) {
            this.poolConfig = DefaultGenericObjectPoolConfig.getDefaultGenericObjectPoolConfig();
        }
        this.connectionPool = new GenericObjectPool(factory, this.poolConfig, DefaultGenericObjectPoolConfig.getDefaultAbandonedConfig());
    }
    
    @Override
    public Connection borrowObject(ResponseFuture future, int timeout) throws Exception {
        Connection connection = connectionPool.borrowObject(timeout);
        connection.setResponseFuture(future);
        return connection;
    }

    @Override
    public Connection borrowObject(ResponseFuture future) throws Exception {
        Connection connection = connectionPool.borrowObject();
        connection.setResponseFuture(future);
        return connection;
    }

    @Override
    public void returnObject(Connection obj) {
        connectionPool.returnObject(obj);
    }

    @Override
    public void invalidateObject(Connection obj) throws Exception {
        connectionPool.invalidateObject(obj);
    }

    @Override
    public void shutdown() {
        connectionPool.close();
    }

}
