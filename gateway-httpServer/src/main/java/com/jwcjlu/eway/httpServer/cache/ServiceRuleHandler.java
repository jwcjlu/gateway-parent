package com.jwcjlu.gateway.httpServer.cache;

import com.jwcjlu.gateway.api.convert.Handler;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServiceRuleHandler {

    private Map<String, List<ServiceHandler>> pluginServiceHandlerMapper = new ConcurrentHashMap<>();

    public <T extends Handler> T getHandler(String pluginName, String serviceName) {
        List<ServiceHandler> serviceHandlers = pluginServiceHandlerMapper.get(pluginName);
        if (CollectionUtils.isEmpty(serviceHandlers))
            return null;
        List<ServiceHandler> shs = serviceHandlers.stream().filter((serviceHandler -> {
            return serviceHandler.getService().equals(serviceName);
        })).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shs))
            return null;
        return (T)shs.get(0).getHandler();
    }

    public List<ServiceHandler> getServiceHandler(String plugin) {
        return pluginServiceHandlerMapper.get(plugin);
    }

    public void pubServiceHandler(String plugin, List<ServiceHandler> serviceHandlers) {
        pluginServiceHandlerMapper.put(plugin, serviceHandlers);
    }

}
