package com.jwcjlu.gateway.httpServer.netty;

import com.jwcjlu.gateway.common.util.OSUtil;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * NioEventLoopGroup管理
 * 方便jvm退出的时候关闭这些线程
 * 和在开启proxytoServer通道的时候线程复用
 */
public class EventLoopGroupManager {
    private Logger LOG = LoggerFactory.getLogger(DefaultHttpProxyServer.class);
    private EventLoopGroup incomingAcceptorThreads;
    private EventLoopGroup incomingWorkerThreads;
    private EventLoopGroup outgoingWorkerThreads;
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public EventLoopGroupManager(int incomingAcceptorThreadnum, int incomingWorkerThreadnum, int outgoingWorkerThreadnum) {
        if(OSUtil.isWindows()) {
            incomingAcceptorThreads = new NioEventLoopGroup(incomingAcceptorThreadnum);
            incomingWorkerThreads = new NioEventLoopGroup(incomingWorkerThreadnum);
            outgoingWorkerThreads = new NioEventLoopGroup(outgoingWorkerThreadnum);
        }else{
            incomingAcceptorThreads = new EpollEventLoopGroup(incomingAcceptorThreadnum);
            incomingWorkerThreads = new EpollEventLoopGroup(incomingWorkerThreadnum);
            outgoingWorkerThreads = new EpollEventLoopGroup(outgoingWorkerThreadnum);
        }

    }

    public EventLoopGroup getIncomingAcceptorThreads() {
        return incomingAcceptorThreads;
    }


    public EventLoopGroup getIncomingWorkerThreads() {
        return incomingWorkerThreads;
    }


    public EventLoopGroup getOutgoingWorkerThreads() {
        return outgoingWorkerThreads;
    }

    public void unregister(boolean graceful) {
        shutdown(graceful);
    }

    private void shutdown(boolean graceful) {
        if (!stopped.compareAndSet(false, true)) {
            LOG.info("Shutdown requested, but ServerGroup is already stopped. Doing nothing.");

            return;
        }

        LOG.info("Shutting down server group event loops " + (graceful ? "(graceful)" : "(non-graceful)"));
        List<EventLoopGroup> allEventLoopGroups = new ArrayList<>();
        if (incomingAcceptorThreads != null) {
            allEventLoopGroups.add(incomingAcceptorThreads);
        }
        if (incomingWorkerThreads != null) {
            allEventLoopGroups.add(incomingAcceptorThreads);
        }
        if (outgoingWorkerThreads != null) {
            allEventLoopGroups.add(incomingAcceptorThreads);
        }
        for (EventLoopGroup group : allEventLoopGroups) {
            if (graceful) {
                group.shutdownGracefully();
            } else {
                group.shutdownGracefully(0, 0, TimeUnit.SECONDS);
            }
        }

        if (graceful) {
            for (EventLoopGroup group : allEventLoopGroups) {
                try {
                    group.awaitTermination(60, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    LOG.warn("Interrupted while shutting down event loop");
                }
            }
        }

        LOG.debug("Done shutting down server group");
    }
}
