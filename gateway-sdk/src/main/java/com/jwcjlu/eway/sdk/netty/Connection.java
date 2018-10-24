package com.jwcjlu.gateway.sdk.netty;

import com.jwcjlu.gateway.sdk.future.ResponseFuture;
import com.jwcjlu.gateway.sdk.http.Request;

public interface Connection {
    void disconnect();

    boolean isActive();

    void setResponseFuture(ResponseFuture future);

    void write(Request request);

    void returnConnection();

}
