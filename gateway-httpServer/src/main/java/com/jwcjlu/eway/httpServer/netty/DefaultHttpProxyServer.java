package com.jwcjlu.gateway.httpServer.netty;


import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.util.OSUtil;
import com.jwcjlu.gateway.httpServer.netty.connection.ClientToProxyConnection;
import com.jwcjlu.gateway.httpServer.handler.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultHttpProxyServer implements HttpProxyServer {
    private Logger LOG = LoggerFactory.getLogger(DefaultHttpProxyServer.class);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private volatile InetSocketAddress boundAddress;
    private volatile int connectTimeout = 4000;
    private volatile int idleConnectionTimeout = 120;
    private int maxInitialLineLength = 16384;
    private int maxHeaderSize = 16384 * 2;
    private int maxChunkSize = 16384 * 2;
    private int maxContentLength = 1024 * 1024 * 64;

    private int incomingAcceptorThreadnum = 2;
    private int incomingWorkerThreadnum = Runtime.getRuntime().availableProcessors() * 4;
    private int outgoingWorkerThreadnum = Runtime.getRuntime().availableProcessors() * 4;
    private final EventLoopGroupManager eventLoopGroupManager;
    private final HttpHandler httpHandler;

    public DefaultHttpProxyServer(int connectTimeout,
                                  int idleConnectionTimeout,
                                  int maxInitialLineLength,
                                  int maxHeaderSize,
                                  int maxChunkSize,
                                  int incomingAcceptorThreadnum,
                                  int incomingWorkerThreadnum,
                                  int outgoingWorkerThreadnum, HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        this.connectTimeout = connectTimeout;
        this.idleConnectionTimeout = idleConnectionTimeout;
        this.maxInitialLineLength = maxInitialLineLength;
        this.maxHeaderSize = maxHeaderSize;
        this.maxChunkSize = maxChunkSize;
        this.incomingAcceptorThreadnum = incomingAcceptorThreadnum;
        this.incomingWorkerThreadnum = incomingWorkerThreadnum;
        this.outgoingWorkerThreadnum = outgoingWorkerThreadnum;
        eventLoopGroupManager = new EventLoopGroupManager(incomingAcceptorThreadnum, incomingWorkerThreadnum, outgoingWorkerThreadnum);
    }

    public DefaultHttpProxyServer(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        eventLoopGroupManager = new EventLoopGroupManager(incomingAcceptorThreadnum, incomingWorkerThreadnum, outgoingWorkerThreadnum);
    }

    public void start(String address, int port) {
        boundAddress = new InetSocketAddress(address, port);
        doStart();

    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxInitialLineLength() {
        return maxInitialLineLength;
    }

    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    /**
     * 管理所有的channel，方便系统关闭的时候释放所有的channel
     */

    private final ChannelGroup allChannels = new DefaultChannelGroup("HTTP-Proxy-Server", GlobalEventExecutor.INSTANCE);

    public void registerChannel(Channel channel) {
        allChannels.add(channel);
    }

    public void remove(Channel channel) {
        allChannels.remove(channel);
    }


    private final Thread jvmShutdownHook = new Thread(new Runnable() {
        @Override
        public void run() {
            abort();
        }
    }, "Eway-JVM-shutdown-hook");

    protected void closeAllChannels(boolean graceful) {
        LOG.info("Closing all channels " + (graceful ? "(graceful)" : "(non-graceful)"));

        ChannelGroupFuture future = allChannels.close();

        // if this is a graceful shutdown, log any channel closing failures. if this isn't a graceful shutdown, ignore them.
        if (graceful) {
            try {
                future.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                LOG.warn("Interrupted while waiting for channels to shut down gracefully.");
            }

            if (!future.isSuccess()) {
                for (ChannelFuture cf : future) {
                    if (!cf.isSuccess()) {
                        LOG.info("Unable to close channel.  Cause of failure for {} is {}", cf.channel(), cf.cause());
                    }
                }
            }
        }
    }

    protected void doStop(boolean graceful) {
        // only stop the server if it hasn't already been stopped
        if (stopped.compareAndSet(false, true)) {
            if (graceful) {
                LOG.info("Shutting down proxy server gracefully");
            } else {
                LOG.info("Shutting down proxy server immediately (non-graceful)");
            }
            eventLoopGroupManager.unregister(graceful);
            closeAllChannels(graceful);
            // remove the shutdown hook that was added when the proxy was started, since it has now been stopped
            BootServiceManager.INSTANCE.shutdown();
            try {
                Runtime.getRuntime().removeShutdownHook(jvmShutdownHook);
            } catch (IllegalStateException e) {
                // ignore -- IllegalStateException means the VM is already shutting down
            }

            LOG.info("Done shutting down proxy server");
        }
    }

    private void doStart() {
        ServerBootstrap serverBootstrap = new ServerBootstrap().group(
            eventLoopGroupManager.getIncomingAcceptorThreads(),
            eventLoopGroupManager.getIncomingWorkerThreads());

        ChannelInitializer<Channel> initializer = new ChannelInitializer<Channel>() {
            protected void initChannel(Channel ch) throws Exception {
                new ClientToProxyConnection(
                    DefaultHttpProxyServer.this,
                    ch.pipeline());
            }

            ;
        };
        if (OSUtil.isWindows()) {
            serverBootstrap.channel(NioServerSocketChannel.class);
        } else {
            serverBootstrap.channel(EpollServerSocketChannel.class);
        }
        serverBootstrap.childHandler(initializer);
        ChannelFuture future = serverBootstrap.bind(boundAddress)
            .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future)
                    throws Exception {
                    if (future.isSuccess()) {
                        registerChannel(future.channel());
                    }
                }
            }).awaitUninterruptibly();

        Throwable cause = future.cause();
        if (cause != null) {
            throw new RuntimeException(cause);
        }

        this.boundAddress = ((InetSocketAddress) future.channel().localAddress());
        LOG.info("Eway started at address: " + this.boundAddress);

        Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
    }

    @Override
    public int getIdleConnectionTimeout() {
        return idleConnectionTimeout;
    }

    @Override
    public void setIdleConnectionTimeout(int timeout) {
        this.idleConnectionTimeout = timeout;

    }

    @Override
    public int getConnectionTimeout() {
        return connectTimeout;
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        this.connectTimeout = timeout;
    }

    @Override
    public void stop() {
        doStop(true);

    }

    @Override
    public void abort() {
        doStop(false);
    }

    @Override
    public InetSocketAddress getListenAddress() {
        return boundAddress;
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public EventLoopGroupManager getEventLoopGroupManager() {
        return eventLoopGroupManager;
    }
}
