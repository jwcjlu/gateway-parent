package com.jwcjlu.gateway.httpServer.netty.connectionpool;

import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.netty.connection.ProxyToServerConnection;


public interface ConnectionPool {
    /**
     * 借用通道
     *
     * @return
     * @throws Exception
     */
    ProxyToServerConnection borrowObject(HttpHandlerContent httpHandlerContent) throws Exception;


    /**
     * 借用通道
     *
     * @return
     * @throws Exception
     */
    ProxyToServerConnection borrowObject(HttpHandlerContent httpHandlerContent, long timeout) throws Exception;

    /**
     * 归还通道
     *
     * @param obj
     */
    void returnObject(ProxyToServerConnection obj);

    /**
     * @param obj
     * @throws Exception
     */
    void invalidateObject(ProxyToServerConnection obj) throws Exception;

    /**
     *
     */
    void shutdown();


}
