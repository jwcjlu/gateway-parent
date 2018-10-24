package com.jwcjlu.gateway.sdk.http;


import com.jwcjlu.gateway.sdk.exception.HeaderValidataException;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.StringUtil;


public class Request {
    private Header header;
    private String context;
    private String uri;
    private MethodAction action;
    private HttpVersion version;

    public Request(Header header, String context, String uri, MethodAction action, HttpVersion version) {
        this.context = context;
        this.header = header;
        this.uri = uri;
        this.action = action;
        this.version = version;
    }

    public Request(String context, String uri, MethodAction action, HttpVersion version) {
        this.context = context;
        this.header = header;
        this.uri = uri;
        this.action = action;
        this.version = version;
    }
    public void validateHeader() throws HeaderValidataException {

        if (StringUtil.isNullOrEmpty(header.getServerKey())) {
            throw new HeaderValidataException("serverKey Can not be empty or null");

        }
        if (StringUtil.isNullOrEmpty(header.getProtocol())) {
            throw new HeaderValidataException("protocol Can not be empty or null");
        }
        if (StringUtil.isNullOrEmpty(header.getModule())) {
            throw new HeaderValidataException("module Can not be empty or null");
        }

    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MethodAction getAction() {
        return action;
    }

    public void setAction(MethodAction action) {
        this.action = action;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public static RequestBuilder get() {
        return new RequestBuilder(MethodAction.GET);
    }

    public static RequestBuilder post() {
        return new RequestBuilder(MethodAction.POST);
    }

    public static RequestBuilder delete() {
        return new RequestBuilder(MethodAction.DELETE);
    }

    public static RequestBuilder put() {
        return new RequestBuilder(MethodAction.PUT);
    }
}
