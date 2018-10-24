package com.jwcjlu.gateway.httpServer.filter;

import com.jwcjlu.gateway.httpServer.adapter.InitializeBootServiceAdapter;
import com.jwcjlu.gateway.httpServer.filter.support.ConcurrencyControlFilter;
import com.jwcjlu.gateway.httpServer.filter.support.DivideFilter;
import com.jwcjlu.gateway.httpServer.filter.support.PostConcurrencyCounterFilter;

import java.util.Properties;

public class FilterBootService extends InitializeBootServiceAdapter {
    @Override
    public void initialize(Properties properties) {
        new DivideFilter();
        new ConcurrencyControlFilter();
        new PostConcurrencyCounterFilter();
    }

}
