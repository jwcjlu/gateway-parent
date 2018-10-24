package com.jwcjlu.gateway.httpServer.netty.connection;


import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.filter.HttpFilters;
import com.jwcjlu.gateway.httpServer.handler.DefaultHttpHandler;
import com.jwcjlu.gateway.httpServer.netty.DefaultHttpProxyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

/**
 * 客户端到网关的代理通道
 */

public class ClientToProxyConnection extends ProxyConnection<HttpRequest> {
    private Logger LOG = LoggerFactory.getLogger(ProxyConnection.class);
    private DefaultHttpProxyServer httpProxyServer;

    /**
     * 以过滤链的形式处理上次的业务需求
     * pre类型的过滤器是处理转发之前的处理eg：限流、熔断、并发控制等属于这种类型
     * route类型主要处理转发的
     * post类型主要转发成功后的后续处理
     *
     * @param msg
     */
    @Override
    protected void readHttpInitial(HttpRequest msg) {
            BootServiceManager.INSTANCE.findService(DefaultHttpHandler.class).dispatcherHandler(
                new HttpHandlerContent(msg,this, HttpFilters.INSTANCE.getPostFilter()));
    }

    @Override
    protected void handlerExcption(Throwable cause) {
        try {
            if (cause instanceof IOException) {
                // IOExceptions are expected errors, for example when a server drops the connection. rather than flood
                // the logs with stack traces for these expected exceptions, log the message at the INFO level and the
                // stack trace at the DEBUG level.
                LOG.info("An IOException occurred on ClientToProxyConnection: " + cause.getMessage());
                LOG.debug("An IOException occurred on ClientToProxyConnection", cause);
            } else if (cause instanceof RejectedExecutionException) {
                LOG.info("An executor rejected a read or write operation on the ClientToProxyConnection (this is normal if the proxy is shutting down). Message: " + cause.getMessage());
                LOG.debug("A RejectedExecutionException occurred on ClientToProxyConnection", cause);
            } else {
                LOG.error("Caught an exception on ClientToProxyConnection", cause);
            }
        } finally {

            LOG.info("Disconnecting open connection to server");
            disconnect();

        }
    }

    public ClientToProxyConnection(DefaultHttpProxyServer proxyServer, ChannelPipeline pipeline) {
        this.httpProxyServer = proxyServer;
        initChannelPipeline(pipeline);
    }

    private void initChannelPipeline(ChannelPipeline pipeline) {

        LOG.debug("Configuring ChannelPipeline");
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder(
            httpProxyServer.getMaxInitialLineLength(),
            httpProxyServer.getMaxHeaderSize(),
            httpProxyServer.getMaxChunkSize()));
        pipeline.addLast("aggregator"
            , new HttpObjectAggregator(httpProxyServer.getMaxContentLength()));
        pipeline.addLast(
            "idle",
            new IdleStateHandler(0, 0, httpProxyServer
                .getIdleConnectionTimeout()));

        pipeline.addLast("handler", this);
    }

    /**
     * 响应客户端
     *
     * @param response
     */
    public void respond(HttpResponse response) {
        writeToChannel(response);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
            if (evt instanceof IdleStateEvent) {
                LOG.debug("Got idle");
                timeout();
            }
        } finally {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            super.channelActive(ctx);
            httpProxyServer.registerChannel(ctx.channel());
        } finally {
            super.channelActive(ctx);
        }
    }
}
