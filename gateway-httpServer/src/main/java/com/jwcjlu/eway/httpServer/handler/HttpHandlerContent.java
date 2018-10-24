package com.jwcjlu.gateway.httpServer.handler;

import com.jwcjlu.gateway.httpServer.exception.FilterException;
import com.jwcjlu.gateway.httpServer.filter.DefaultFilterChain;
import com.jwcjlu.gateway.httpServer.filter.HttpFilter;
import com.jwcjlu.gateway.httpServer.netty.connection.ClientToProxyConnection;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpHandlerContent {
    private HttpRequest request;
    private HttpResponse response;
    private Map<String, Object> attributes = new HashMap<>();
    private final ClientToProxyConnection proxyConnection;
    private final List<HttpFilter> postFilter;
    private final ScheduledFuture future;
    private volatile AtomicBoolean isResponse = new AtomicBoolean(false);
    private long DEFAULT_HANDLER_TIMEOUT = 2 * 60 * 1000;

    public HttpHandlerContent(HttpRequest httpRequest, HttpResponse httpResponse, ClientToProxyConnection proxyConnection, List<HttpFilter> filters) {
        this.request = httpRequest;
        this.response = httpResponse;
        this.proxyConnection = proxyConnection;
        this.postFilter = filters;
        future = proxyConnection.createTimeoutTask(DEFAULT_HANDLER_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public HttpHandlerContent(HttpRequest httpRequest, ClientToProxyConnection proxyConnection, List<HttpFilter> filters) {
        this.request = httpRequest;
        this.proxyConnection = proxyConnection;
        this.postFilter = filters;
        future = proxyConnection.createTimeoutTask(DEFAULT_HANDLER_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    public <T> T getAttribute(String name, T t) {
        if (Objects.isNull(getAttribute(name))) {
            return t;
        }
        return (T) attributes.get(name);
    }

    public <T> void attribute(String name, T t) {
        attributes.put(name, t);
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public ClientToProxyConnection getProxyConnection() {
        return proxyConnection;
    }

    public AtomicBoolean isResponse() {
        return isResponse;
    }


    public void response(HttpResponse httpResponse) {
        if (!Objects.isNull(httpResponse)) {
            this.response = httpResponse;
        }

        try {
            future.cancel(true);
            if (!CollectionUtils.isEmpty(postFilter)) {
                new DefaultFilterChain(postFilter).doFilter(this);
            }

        } catch (FilterException e) {

        } finally {
            proxyConnection.writeToChannel(response);
            isResponse.compareAndSet(false, true);
        }

    }

    public void response() {
        response(null);
    }


}
