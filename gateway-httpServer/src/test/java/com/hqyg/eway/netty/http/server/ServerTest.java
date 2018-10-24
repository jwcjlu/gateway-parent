package com.jwcjlu.gateway.netty.http.server;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.netty.DefaultHttpProxyServer;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionPoolManager;
import com.jwcjlu.gateway.httpServer.filter.HttpFilters;
import com.jwcjlu.gateway.httpServer.handler.DefaultHttpHandler;


public class ServerTest {

    public static void main(String[] agrs) {
        DefaultHttpProxyServer server=   new DefaultHttpProxyServer(new DefaultHttpHandler());
        ConnectionPoolManager.INSTANCE.setExecutors(server.getEventLoopGroupManager().getOutgoingWorkerThreads());
        HttpFilters.INSTANCE.registerFilter("",new RoutingFilterSimple(new ServerInfo("10.60.38.51",8081)));
        server.start("localhost",8085);


    }
}































