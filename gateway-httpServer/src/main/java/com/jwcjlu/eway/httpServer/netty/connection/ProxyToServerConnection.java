package com.jwcjlu.gateway.httpServer.netty.connection;

import com.jwcjlu.gateway.common.util.OSUtil;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionFactory;
import com.jwcjlu.gateway.httpServer.netty.util.ChannelUtils;
import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import io.netty.bootstrap.Bootstrap;

import io.netty.channel.*;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 网关到真正服务器的代理通道
 */
public class ProxyToServerConnection extends ProxyConnection<HttpResponse> {
    private Logger LOG = LoggerFactory.getLogger(ProxyConnection.class);
    private volatile HttpHandlerContent httpHandlerContent;
    private Bootstrap bootstrap;
    private final ConnectionFactory factory;
    public static final String READ_TIMEOUT_HANDLER_NAME = "readTimeoutHandler";

    public ProxyToServerConnection(ServerInfo origin, ConnectionFactory factory, EventLoopGroup eventExecutors) throws InterruptedException {
        super();
        this.factory = factory;
        bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors);
        if (OSUtil.isWindows()) {
            bootstrap.channel(NioSocketChannel.class);
        } else {
            bootstrap.channel(EpollSocketChannel.class);
        }
        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            protected void initChannel(Channel ch) throws Exception {
                initChannelPipeline(ch.pipeline());
            }

            ;
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

    @Override
    protected void readHttpInitial(HttpResponse httpResponse) {
        LOG.debug("Received raw response: {}", httpResponse);
        if (httpResponse.getDecoderResult().isFailure()) {
            LOG.debug("Could not parse response from server. Decoder result: {}", httpResponse.getDecoderResult().toString());
            FullHttpResponse substituteResponse = HttpUtils.INSTANCE.createFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.BAD_GATEWAY, "Unable to parse response from server");
            HttpHeaders.setKeepAlive(substituteResponse, false);
            httpResponse = substituteResponse;
        }
        respondWith(httpResponse);

    }

    private void respondWith(HttpResponse httpResponse) {
        try {
            httpHandlerContent.response(httpResponse);
            removeReadTimeoutHandler();
        } finally {
            factory.getConnection().returnObject(this);

        }

    }

    @Override
    protected void handlerExcption(Throwable cause) {
        try {
            if (cause instanceof IOException) {
                LOG.info("An IOException occurred on ProxyToServerConnection: " + cause.getMessage());
                LOG.debug("An IOException occurred on ProxyToServerConnection", cause);
            } else if (cause instanceof RejectedExecutionException) {
                LOG.info("An executor rejected a read or write operation on the ProxyToServerConnection (this is normal if the proxy is shutting down). Message: " + cause.getMessage());
                LOG.debug("A RejectedExecutionException occurred on ProxyToServerConnection", cause);
            } else {
                LOG.error("Caught an exception on ProxyToServerConnection", cause);
            }
            if (cause instanceof TimeoutException) {
                responseWithError("server timeout ", HttpResponseStatus.GATEWAY_TIMEOUT);
            } else {
                responseWithError(cause.getMessage(), HttpResponseStatus.BAD_GATEWAY);
            }
        } finally {

            LOG.info("Disconnecting open connection to server");
            disconnect();

        }
    }

    private void initChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("encoder", new HttpRequestEncoder());
        pipeline.addLast("decoder", new HttpResponseDecoder(
            16384,
            16384 * 2,
            16384 * 2
        ));
        pipeline.addLast("aggregator"
            , new HttpObjectAggregator(1024 * 1024 * 64));
        pipeline.addLast("handler", this);
    }

    public void setHttpHandlerContent(HttpHandlerContent httpHandlerContent) {
        this.httpHandlerContent = httpHandlerContent;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // Log some info about this.
            final String msg = "Origin channel for origin  - idle timeout has fired. " + ChannelUtils.channelInfoForLogging(ctx.channel());
            responseWithError(msg, HttpResponseStatus.BAD_GATEWAY);
            removeReadTimeoutHandler();
            factory.getConnection().returnObject(this);
        }
    }

    private void removeReadTimeoutHandler() {
        // Remove (and therefore destroy) the readTimeoutHandler when we release the
        // channel back to the pool. As don't want it timing-out when it's not in use.
        final ChannelPipeline pipeline = channel.pipeline();
        removeHandlerFromPipeline(READ_TIMEOUT_HANDLER_NAME, pipeline);
    }

    private void removeHandlerFromPipeline(String handlerName, ChannelPipeline pipeline) {
        if (pipeline.get(handlerName) != null) {
            pipeline.remove(handlerName);
        }
    }

    public void startReadTimeoutHandler(long readTimeout) {
        channel.pipeline().addBefore("handler", READ_TIMEOUT_HANDLER_NAME, new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS));
    }

    public void startReadTimeoutHandler() {
        startReadTimeoutHandler(2*60 * 1000);
    }

    private void responseWithError(String msg, HttpResponseStatus status) {
        HttpResponse httpResponse = HttpUtils.INSTANCE.createFullHttpResponse(HttpVersion.HTTP_1_1, status, msg);
        if (httpHandlerContent != null && !httpHandlerContent.isResponse().get()) {
            httpHandlerContent.response(httpResponse);
        }

    }

}
