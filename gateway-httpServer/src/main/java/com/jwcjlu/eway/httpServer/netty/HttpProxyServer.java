package com.jwcjlu.gateway.httpServer.netty;

import java.net.InetSocketAddress;

/**
 *
 */
public interface HttpProxyServer {
    /**
     * 通道空闲超时时间
     *
     * @return
     */
    int getIdleConnectionTimeout();

    /**
     * 设置空闲超时时间
     *
     * @param timeout
     */
    void setIdleConnectionTimeout(int timeout);

    /**
     * 获取连接的超时时间
     *
     * @return
     */
    int getConnectionTimeout();

    /**
     * 设置连接的超时时间
     *
     * @param timeout
     * @return
     */
    void setConnectionTimeout(int timeout);

    /**
     * 停止该通道
     */
    void stop();

    /**
     *
     */
    void abort();

    /**
     * 监听地址
     *
     * @return
     */
    InetSocketAddress getListenAddress();


}
