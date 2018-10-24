package com.jwcjlu.gateway.httpServer.netty.connectionpool;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.netty.connection.ProxyToServerConnection;
import io.netty.channel.EventLoopGroup;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConnectionFactory extends BasePooledObjectFactory<ProxyToServerConnection> {
    private Logger logger= LoggerFactory.getLogger(ConnectionFactory.class);

    private final ServerInfo origin;
    private final ConnectionPool connectionPool;
    private EventLoopGroup executors;

    public ConnectionFactory(ServerInfo origin, ConnectionPool connectionPool, EventLoopGroup eventExecutorse) {
        this.origin = origin;
        this.connectionPool = connectionPool;
        this.executors = eventExecutorse;
    }

    @Override
    public ProxyToServerConnection create() throws Exception {
        logger.info("create connection pool");
        System.out.println("create connection pool");
        return new ProxyToServerConnection(origin, this, executors);
    }

    @Override
    public PooledObject<ProxyToServerConnection> wrap(ProxyToServerConnection obj) {
        return new DefaultPooledObject(obj);
    }


    @Override
    public void destroyObject(PooledObject<ProxyToServerConnection> p) throws Exception {
        logger.info("destroy connection pool");
        System.out.println("destroy connection pool");
        p.getObject().disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<ProxyToServerConnection> p) {
        return (p == null || p.getObject() == null) ? false : p.getObject().isActive();
    }

    public ConnectionPool getConnection() {
        return connectionPool;
    }


}
