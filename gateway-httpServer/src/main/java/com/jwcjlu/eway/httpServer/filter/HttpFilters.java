package com.jwcjlu.gateway.httpServer.filter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum HttpFilters {
    INSTANCE;
    private Map<String, HttpFilter> httpFilters = new HashMap<String, HttpFilter>();


    public HttpFilter getHttpFilter(String name) {
        return httpFilters.get(name);

    }

    public void registerFilter(String name, HttpFilter httpFilter) {
        httpFilters.put(name, httpFilter);
    }

    public List<HttpFilter> getPostFilter() {
        if (httpFilters.isEmpty()) {
            return null;
        }
        return httpFilters.values().stream().filter(f -> f.getFilterType() == FilterType.POST).collect(Collectors.toList());
    }

}
