package com.jwcjlu.gateway.sdk.netty.connectionpool;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.sdk.netty.Connection;
import com.jwcjlu.gateway.sdk.netty.ConnectionImpl;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class ConnectionFactory extends BasePooledObjectFactory<Connection> {
    private final ServerInfo origin;
    private final ConnectionPool connectionPool;
    private NioEventLoopGroup executors;

    public ConnectionFactory(ServerInfo origin, ConnectionPool connectionPool, NioEventLoopGroup eventExecutorse) {
        this.origin = origin;
        this.connectionPool = connectionPool;
        this.executors = eventExecutorse;
    }

    @Override
    public Connection create() throws Exception {
        return new ConnectionImpl(origin, executors,connectionPool);
    }

    @Override
    public PooledObject<Connection> wrap(Connection obj) {
        return new DefaultPooledObject<Connection>(obj);
    }


    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        p.getObject().disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        return (p == null || p.getObject() == null) ? false : p.getObject().isActive();
    }

    public ConnectionPool getConnection() {
        return connectionPool;
    }


}
