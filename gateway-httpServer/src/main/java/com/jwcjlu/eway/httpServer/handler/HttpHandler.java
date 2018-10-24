package com.jwcjlu.gateway.httpServer.handler;

/**
 * http请求的调度处理类
 */
public interface HttpHandler {
    void dispatcherHandler(HttpHandlerContent requestContent);
}
