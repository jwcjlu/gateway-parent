package com.jwcjlu.gateway.httpServer.mock;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.filter.HttpFilters;
import com.jwcjlu.gateway.httpServer.netty.DefaultHttpProxyServer;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionPoolManager;
import com.jwcjlu.gateway.httpServer.handler.DefaultHttpHandler;


public class MockServer {
    public static void main(String[] agrs) {
        DefaultHttpProxyServer server=   new DefaultHttpProxyServer(new DefaultHttpHandler());
        ConnectionPoolManager.INSTANCE.setExecutors(server.getEventLoopGroupManager().getOutgoingWorkerThreads());
        HttpFilters.INSTANCE.registerFilter("",new MockRoutingFilter(new ServerInfo("10.60.38.51",8081)));
        server.start("0.0.0.0",8085);


    }
}
