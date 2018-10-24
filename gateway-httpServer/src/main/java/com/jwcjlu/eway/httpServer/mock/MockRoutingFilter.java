package com.jwcjlu.gateway.httpServer.mock;

import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.filter.FilterChain;
import com.jwcjlu.gateway.httpServer.filter.FilterType;
import com.jwcjlu.gateway.httpServer.filter.HttpFilter;
import com.jwcjlu.gateway.httpServer.netty.connection.ClientToProxyConnection;

import com.jwcjlu.gateway.httpServer.netty.connection.ProxyToServerConnection;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionPoolManager;
import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class MockRoutingFilter implements HttpFilter {
    public String name(){
        return "mock";
    }

    private ServerInfo server;
    public MockRoutingFilter(ServerInfo server){
        this.server=server;
    }
    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.ROUTING;
    }

    @Override
    public void doFilter(HttpHandlerContent content, FilterChain chain) throws Exception {
        if(content.getProxyConnection() instanceof ClientToProxyConnection){
            try {
                ClientToProxyConnection connection = content.getProxyConnection();
                ProxyToServerConnection proxyToServerConnection = ConnectionPoolManager.INSTANCE.getConnectionPool(server).borrowObject(content);
                proxyToServerConnection.writeToChannel(content.getRequest());
            }catch (Throwable t){
                t.printStackTrace();
                content.getProxyConnection().writeToChannel(HttpUtils.INSTANCE.createFullHttpResponse(HttpVersion.HTTP_1_1
                    , HttpResponseStatus.BAD_GATEWAY,"server error"));
            }

        }

    }

    @Override
    public void decorationesponse(HttpHandlerContent content) {

    }

    @Override
    public Class<? extends Handler> handlerClass() {
        return null;
    }

}
