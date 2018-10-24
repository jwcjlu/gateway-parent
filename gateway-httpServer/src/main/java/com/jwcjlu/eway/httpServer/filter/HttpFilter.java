package com.jwcjlu.gateway.httpServer.filter;

import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;

public interface HttpFilter {
    /**
     * 过滤器的名称 对应plugin的名称
     *
     * @return
     */
    String name();

    /**
     * 顺序
     *
     * @return
     */

    int getOrder();

    /**
     * 过滤器的类型
     * pre类型是转发之前执行
     * end类型是转发完成之后执行
     *
     * @return
     */
    FilterType getFilterType();

    /**
     * 进行过滤处理
     * @param content
     * @param chain
     * @throws Throwable
     */

    void doFilter(HttpHandlerContent content, FilterChain chain) throws Throwable;

    /**
     * 当一个过滤器抛异常的时候或者不通过的时候，进行修饰response（eg返回自定义的响应码）
     * @param content
     */
    void decorationesponse(HttpHandlerContent content);

    /**
     * 规则的类型
     * @return
     */
     Class<? extends Handler> handlerClass();


}
