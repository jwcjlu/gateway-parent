package com.jwcjlu.gateway.sdk.netty.connectionpool;

import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.netty.Connection;


public interface ConnectionPool {
    /**
     * 借用通道
     *
     * @return
     * @throws Exception
     */
    Connection borrowObject(ResponseFuture future) throws Exception;


    /**
     * 借用通道
     *
     * @return
     * @throws Exception
     */
    Connection borrowObject(ResponseFuture future, int timeout) throws Exception;

    /**
     * 归还通道
     *
     * @param obj
     */
    void returnObject(Connection obj);

    /**
     * @param obj
     * @throws Exception
     */
    void invalidateObject(Connection obj) throws Exception;

    /**
     *
     */
    void shutdown();


}
