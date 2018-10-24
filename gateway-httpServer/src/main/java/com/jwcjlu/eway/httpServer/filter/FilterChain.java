package com.jwcjlu.gateway.httpServer.filter;


import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.exception.FilterException;


public interface FilterChain {
     void doFilter(HttpHandlerContent content) throws FilterException;
}
