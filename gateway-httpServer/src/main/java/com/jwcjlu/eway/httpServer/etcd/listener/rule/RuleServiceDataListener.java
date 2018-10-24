package com.jwcjlu.gateway.httpServer.etcd.listener.rule;


import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.httpServer.cache.ServiceHandler;
import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractDataListener;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RuleServiceDataListener extends AbstractDataListener {
    @Override
    public void handleData(String path, String data) {
        List<ServiceHandler> handlerList = dataCacheManager.getServiceHandlers(EtcdUtil.getParentNode(path));
        if (CollectionUtils.isEmpty(handlerList)) {
            handlerList = new ArrayList<>();
            dataCacheManager.pubServiceHandler(EtcdUtil.getParentNode(path), handlerList);
        }
        Handler handler = dataCacheManager.getHandler(EtcdUtil.getParentNode(path), EtcdUtil.getSimpleName(path));
        if (Objects.isNull(handler)) {
            handlerList.add(new ServiceHandler(path, data));
        } else {
            updateHandler(handlerList, new ServiceHandler(path, data));

        }
    }

    private void updateHandler(List<ServiceHandler> serviceHandlers, ServiceHandler handler) {
        serviceHandlers = serviceHandlers.stream().map(service -> {
                if (service.getService().equals(handler.getService())) {
                    service.setHandler(handler.getHandler());
                }
                return service;

            }
        ).collect(Collectors.toList());

    }
}
