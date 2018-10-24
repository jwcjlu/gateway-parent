package com.jwcjlu.gateway.httpServer.bootstrap;

import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.util.ConfigUtils;
import com.jwcjlu.gateway.httpServer.netty.DefaultHttpProxyServer;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionPoolManager;
import com.jwcjlu.gateway.httpServer.handler.DefaultHttpHandler;

import java.util.Properties;

public class EwayBootstrap {

    public static void main(String[] agrs) throws Exception {
        Properties properties = ConfigUtils.loadProps("config.properties");
        BootServiceManager.INSTANCE.boot(properties);
        DefaultHttpProxyServer server = new DefaultHttpProxyServer(new DefaultHttpHandler());
        ConnectionPoolManager.INSTANCE.setExecutors(server.getEventLoopGroupManager().getOutgoingWorkerThreads());
        server.start(ConfigUtils.getString(properties, "gateway.host"), ConfigUtils.getInt(properties, "gateway.port"));

    }
}
