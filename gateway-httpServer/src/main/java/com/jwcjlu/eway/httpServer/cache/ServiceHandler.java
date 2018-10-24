package com.jwcjlu.gateway.httpServer.cache;

import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.common.util.JsonUtil;
import com.jwcjlu.gateway.httpServer.filter.HttpFilter;
import com.jwcjlu.gateway.httpServer.filter.HttpFilters;
import java.util.Objects;

public class ServiceHandler {


    private String service;
    private Handler handler;

    public ServiceHandler(String service, String handler) {
        this.service = EtcdUtil.getSimpleName(service);
        String pluginName = EtcdUtil.getParentNode(service);
        HttpFilter filter=HttpFilters.INSTANCE.getHttpFilter(pluginName);
       if(Objects.isNull(filter))
           return;
        this.handler = JsonUtil.fromJson(handler,filter.handlerClass());
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return "ServiceHandler{" +
            "service='" + service + '\'' +
            ", handler='" + handler + '\'' +
            '}';
    }

}
