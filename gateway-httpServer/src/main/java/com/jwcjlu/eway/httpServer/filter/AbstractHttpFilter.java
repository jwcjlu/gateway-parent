package com.jwcjlu.gateway.httpServer.filter;

import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;

public abstract class AbstractHttpFilter implements HttpFilter {
    public AbstractHttpFilter() {
        HttpFilters.INSTANCE.registerFilter(name(), this);
    }

    public void decorationesponse(HttpHandlerContent content) {

    }



}
