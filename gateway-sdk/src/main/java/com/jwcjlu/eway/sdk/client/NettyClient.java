package com.jwcjlu.gateway.sdk.client;


import com.jwcjlu.gateway.core.lb.LoadBalanceFactory;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.sdk.callback.Callback;
import com.jwcjlu.gateway.sdk.future.DefaultResponseFuture;
import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.http.Request;
import com.jwcjlu.gateway.sdk.http.Response;
import com.jwcjlu.gateway.sdk.netty.Connection;
import com.jwcjlu.gateway.sdk.netty.connectionpool.ConnectionPoolManager;
import com.jwcjlu.gateway.sdk.service.ServiceDiscovery;

import java.util.concurrent.TimeUnit;

public class NettyClient implements Client {
    private final static long defaultTimeOut = 50000;
    private Connection connection;

    public NettyClient() {
    }

    @Override
    public Response syncRequest(Request request) throws Exception {
        request.validateHeader();
        return syncRequest(request, defaultTimeOut, TimeUnit.MILLISECONDS);

    }

    @Override
    public Response syncRequest(Request request, Long timeout, TimeUnit unit) throws Exception {
        request.validateHeader();
        ResponseFuture future = getConnection(null);
        if (connection == null) {
            return future.get();
        }
        connection.write(request);
        Response response = future.get(timeout);
        return response;
    }

    @Override
    public void asyncRequest(Request request, Callback callback) throws Exception {
        request.validateHeader();
        getConnection(callback);
        if (connection == null) {
            return;
        }
        connection.write(request);
    }


    @Override
    public void cancel() {
        if (connection != null)
            connection.disconnect();
    }

    private ResponseFuture getConnection(Callback callback) {
        ResponseFuture future = new DefaultResponseFuture(callback);
        ServerInfo info = LoadBalanceFactory.INSTANCE.getDefaultLoadBalance().select(ServiceDiscovery.serverInfoList);
        try {
            connection = ConnectionPoolManager.INSTANCE.getConnectionPool(info).borrowObject(future);
        } catch (Exception e) {
            future.receive(new Response(e.toString(), 500, false));
        }
        return future;
    }

}

