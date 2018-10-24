package com.jwcjlu.gateway.httpServer.netty.connectionpool;

import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.netty.connection.ProxyToServerConnection;
import io.netty.channel.EventLoopGroup;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Objects;

public class DefaultConnectionPool implements ConnectionPool {
    private final ServerInfo origin;
    private GenericObjectPool<ProxyToServerConnection> connectionPool;
    private GenericObjectPoolConfig poolConfig;
    private BasePooledObjectFactory<ProxyToServerConnection> factory;

    public DefaultConnectionPool(ServerInfo server, GenericObjectPoolConfig poolConfig, EventLoopGroup eventExecutors) {
        this.origin = server;
        this.factory = new ConnectionFactory(server, this, eventExecutors);
        this.poolConfig=poolConfig;
        if (Objects.isNull(poolConfig)) {
            this.poolConfig = BootServiceManager.INSTANCE.findService(DefaultGenericObjectPoolConfig.class).getDefaultGenericObjectPoolConfig();
        }
        this.connectionPool = new GenericObjectPool(factory, this.poolConfig, BootServiceManager.INSTANCE.findService(DefaultGenericObjectPoolConfig.class)
            .getDefaultAbandonedConfig());
    }


    @Override
    public ProxyToServerConnection borrowObject(HttpHandlerContent httpHandlerContent, long timeout) throws Exception {
        ProxyToServerConnection connection = connectionPool.borrowObject();
        connection.startReadTimeoutHandler(timeout);
        connection.setHttpHandlerContent(httpHandlerContent);
        return connection;
    }

    @Override
    public ProxyToServerConnection borrowObject(HttpHandlerContent httpHandlerContent) throws Exception {
        ProxyToServerConnection connection = connectionPool.borrowObject();
        connection.startReadTimeoutHandler();
        connection.setHttpHandlerContent(httpHandlerContent);
        return connection;
    }

    @Override
    public void returnObject(ProxyToServerConnection obj) {
        connectionPool.returnObject(obj);
    }

    @Override
    public void invalidateObject(ProxyToServerConnection obj) throws Exception {
        connectionPool.invalidateObject(obj);
    }

    @Override
    public void shutdown() {
        connectionPool.close();
    }

}
