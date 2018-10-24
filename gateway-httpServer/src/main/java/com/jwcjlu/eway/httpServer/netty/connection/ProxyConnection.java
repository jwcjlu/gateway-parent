package com.jwcjlu.gateway.httpServer.netty.connection;

import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 通道代理的封装，分客户端到网关和网关到真正服务端二种通道
 * 这里做简单测封装超时处理和断开连接的处理
 *
 * @param <I>
 */
public abstract class ProxyConnection<I extends HttpObject> extends SimpleChannelInboundHandler<Object> {
    private AtomicBoolean writeable = new AtomicBoolean(false);
    private Logger LOG = LoggerFactory.getLogger(ProxyConnection.class);
    protected volatile Channel channel;
    protected volatile ChannelHandlerContext ctx;
    private long lastReadTime;

    protected ProxyConnection() {
        this.lastReadTime = System.currentTimeMillis();

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

    public void timeout() {
        disconnect();
    }

    private void read(Object msg) {
        lastReadTime = System.currentTimeMillis();
        if (msg instanceof HttpObject) {
            HttpObject obj = (HttpObject) msg;
            readHttpInitial((I) obj);
        }
    }

    protected abstract void readHttpInitial(I msg);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        read(msg);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            channel = ctx.channel();
            this.ctx = ctx;
            writeable = new AtomicBoolean(true);
        } finally {
            super.channelActive(ctx);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            disconnect();
        } finally {
            super.channelInactive(ctx);
        }

    }


    protected abstract void handlerExcption(Throwable cause);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        handlerExcption(cause);
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 如果msg是ReferenceCounted的实例话 这里的引用计数器应该加1，否则其他线程计算出引用为0而回收
     * 该对象，导致异常io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
     *
     * @param msg
     * @return
     */
    public ChannelFuture writeToChannel(final Object msg) {
        if (msg instanceof ReferenceCounted) {
            LOG.debug("Retaining reference counted message");
            ((ReferenceCounted) msg).retain();
        }
        return channel.writeAndFlush(msg);
    }

    public boolean isActive() {
        return channel == null ? false : channel.isActive();
    }

    public ScheduledFuture createTimeoutTask(long timeout, TimeUnit unit) {
        return channel.eventLoop().schedule(() -> writeToChannel(HttpUtils.INSTANCE.createTimeoutResponse()), timeout, unit);
    }

}
