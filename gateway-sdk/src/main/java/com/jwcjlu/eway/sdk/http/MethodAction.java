package com.jwcjlu.gateway.sdk.http;

import io.netty.handler.codec.http.HttpMethod;

public enum MethodAction {
    POST,GET,PUT,DELETE;
    public HttpMethod toHttpMethod(){
        return HttpMethod.valueOf(this.name());
    }
}
