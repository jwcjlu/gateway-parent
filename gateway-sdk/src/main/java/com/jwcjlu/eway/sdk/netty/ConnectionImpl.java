package com.jwcjlu.gateway.sdk.netty;

import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.http.Request;
import com.jwcjlu.gateway.sdk.netty.connectionpool.ConnectionPool;
import com.jwcjlu.gateway.sdk.netty.handler.HttpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class ConnectionImpl implements Connection {
    private Bootstrap bootstrap;
    private HttpHandler handler;
    private ConnectionPool connectionPool;

    public ConnectionImpl(ServerInfo origin, EventLoopGroup group, ConnectionPool connectionPool) throws InterruptedException {
        bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        this.connectionPool = connectionPool;
        final ConnectionImpl ci = this;
        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            protected void initChannel(Channel ch) throws Exception {
                handler = new HttpHandler(ch.pipeline(), ci);
            }
        };
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
            // 如果是延时敏感型应用，建议关闭Nagle算法
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
        bootstrap.handler(initializer);
        ChannelFuture future = bootstrap.connect(origin.getHost(), origin.getPort()).sync();
        if (future.isSuccess()) {
            future.await(1000, TimeUnit.MILLISECONDS);
        }
    }

    public void disconnect() {
        handler.disconnect();
        bootstrap = null;
    }

    public boolean isActive() {
        return handler.isActive();
    }

    public void setResponseFuture(ResponseFuture future) {
        handler.setResponseFuture(future);
    }

    public void write(Request request) {
        handler.write(request);
    }

    public void returnConnection() {
        connectionPool.returnObject(this);
    }

}
