package com.jwcjlu.gateway.sdk.netty.handler;
import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.http.Request;
import com.jwcjlu.gateway.sdk.http.Response;
import com.jwcjlu.gateway.sdk.netty.Connection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.StringUtil;


public class HttpHandler extends SimpleChannelInboundHandler<HttpResponse> {
    private ResponseFuture future;
    private volatile Channel channel;
    private volatile ChannelHandlerContext ctx;
    private Connection connection;

    public HttpHandler(ChannelPipeline pipeline, Connection connection) {
        initChannelPipeline(pipeline);
        this.connection = connection;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpResponse msg) throws Exception {
        try {
            reply(msg);
        } finally {
            connection.returnConnection();
            this.future=null;
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            channel = ctx.channel();
            this.ctx = ctx;
        } finally {
            super.channelActive(ctx);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            channel = ctx.channel();
            this.ctx = ctx;
        } finally {
            super.channelInactive(ctx);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    public void disconnect() {
        if (channel == null) {

        } else {
            final Promise<Void> promise = channel.newPromise();
            writeToChannel(Unpooled.EMPTY_BUFFER).addListener(
                new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(
                        Future<? super Void> future)
                        throws Exception {
                        closeChannel(promise);
                    }
                });

        }
    }

    /**
     * 如果msg是ReferenceCounted的实例话 这里的引用计数器应该加1，否则其他线程计算出引用为0而回收
     * 该对象，导致异常io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
     *
     * @param msg
     * @return
     */
    private ChannelFuture writeToChannel(final Object msg) {
        if (msg instanceof ReferenceCounted) {
            ((ReferenceCounted) msg).retain();
        }
        return channel.writeAndFlush(msg);
    }

    public void closeChannel(Promise<Void> promise) {
        channel.close().addListener(
            new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(
                    Future<? super Void> future)
                    throws Exception {
                    if (future
                        .isSuccess()) {
                        promise.setSuccess(null);
                    } else {
                        promise.setFailure(future
                            .cause());
                    }
                }

                ;
            });
    }

    public boolean isActive() {
        return channel == null ? false : channel.isActive();
    }

    public void setResponseFuture(ResponseFuture future) {
        this.future = future;
    }

    public void write(Request request) {
        ByteBuf content=null;

        DefaultFullHttpRequest httpRequest= new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,request.getAction().toHttpMethod(),
            request.getUri());
        if(!StringUtil.isNullOrEmpty(request.getContext())){
            httpRequest.content().writeBytes(request.getContext().getBytes());
        }
        request.getHeader().toHttpHeaders(httpRequest.headers());
        httpRequest.headers().add("content-length",httpRequest.content().readableBytes());
        writeToChannel(httpRequest);
    }
    private void  reply(HttpResponse msg){
        future.receive(Response.toResponse(msg));
    }
}
