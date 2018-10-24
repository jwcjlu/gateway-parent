package com.jwcjlu.gateway.sdk.http;

import io.netty.handler.codec.http.HttpVersion;

public class RequestBuilder {
    private MethodAction action;
    private HttpVersion version = HttpVersion.HTTP_1_1;
    private String context;
    private String uri;
    private Header header;

    public RequestBuilder(MethodAction action) {
        this.action = action;
    }

    public RequestBuilder uri(String uri) {
        this.uri = uri;
        return this;
    }
    public RequestBuilder header(Header header) {
        this.header = header;
        return this;
    }
    public RequestBuilder context(String context) {
        this.context = context;
        return this;
    }

    public Request build() {
        return new Request(header,context, uri, action, version);
    }
}
