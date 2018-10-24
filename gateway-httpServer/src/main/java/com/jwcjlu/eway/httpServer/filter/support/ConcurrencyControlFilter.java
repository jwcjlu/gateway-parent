package com.jwcjlu.gateway.httpServer.filter.support;

import com.jwcjlu.gateway.api.convert.ConcurrencyHandle;
import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.httpServer.DTO.RequestDTO;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.exception.FilterException;
import com.jwcjlu.gateway.httpServer.filter.AbstractHttpFilter;
import com.jwcjlu.gateway.httpServer.filter.FilterChain;
import com.jwcjlu.gateway.httpServer.filter.FilterType;
import com.jwcjlu.gateway.httpServer.filter.support.counter.ConcurrencyCounter;
import com.jwcjlu.gateway.httpServer.filter.support.counter.ConcurrencyCounterFactory;
import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyControlFilter extends AbstractHttpFilter {
    private ConcurrentMap<String, AtomicInteger> serviceKeyMap = new ConcurrentHashMap<>();

    @Override
    public String name() {
        return "concurrency";
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.PRE;
    }

    @Override
    public void doFilter(HttpHandlerContent content, FilterChain chain) throws Throwable {
        RequestDTO requestDTO = content.getAttribute(Constants.REQUESTDTO);
        String serviceKey = requestDTO.getServerKey();
        ConcurrencyHandle handle = BootServiceManager.INSTANCE.findService(DataCacheManager.class).getHandler(name(), requestDTO.getServerKey());
        if (Objects.isNull(handle)) {
            throw new FilterException("No rule found, processing failed");
        }
        ConcurrencyCounterFactory.INSTANCE.register(serviceKey, handle.getConcurrencyType());
        ConcurrencyCounter concurrencyCounter = ConcurrencyCounterFactory.INSTANCE.getConcurrencyCounter(serviceKey);
        if (concurrencyCounter.isAllow(serviceKey, handle.getBurstCapacity(), 1)) {
            content.attribute(Constants.CONCURRENCY_CONTROL, true);
        } else {
            throw new FilterException();
        }

    }

    @Override
    public void decorationesponse(HttpHandlerContent content) {
        content.setResponse(HttpUtils.INSTANCE
            .createFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS, "Too many requests, " +
                "in order to protect the server, refuse to request"));

    }

    public Class<? extends Handler> handlerClass() {
        return ConcurrencyHandle.class;
    }
}
