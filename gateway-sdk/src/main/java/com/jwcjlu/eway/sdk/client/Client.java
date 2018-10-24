package com.jwcjlu.gateway.sdk.client;

import com.jwcjlu.gateway.sdk.callback.Callback;
import com.jwcjlu.gateway.sdk.http.Request;
import com.jwcjlu.gateway.sdk.http.Response;

import java.util.concurrent.TimeUnit;

public interface Client {
    /**
     *
     * @param request
     */
    Response syncRequest(Request request) throws Exception;
    /**
     *
     * @param request
     */
    Response syncRequest(Request request, Long timeout, TimeUnit unit)throws Exception;

    /**
     * 异步调用
     * @param request
     * @param callback
     */
    void asyncRequest(Request request, Callback callback)throws Exception;
    /**
     * cancel
     */
    void  cancel();

}
