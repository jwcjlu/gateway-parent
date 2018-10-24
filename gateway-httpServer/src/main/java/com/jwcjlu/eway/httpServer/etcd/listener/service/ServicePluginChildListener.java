package com.jwcjlu.gateway.httpServer.etcd.listener.service;


import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractChildListener;

import java.util.List;
import java.util.stream.Collectors;

public class ServicePluginChildListener extends AbstractChildListener {
    @Override
    public void handlerChildChanged(String path, List<String> children) {
        List<String> plugins = children.stream().map((c) -> {
            return EtcdUtil.getSimpleName(c);
        }).collect(Collectors.toList());
        dataCacheManager.putServicePlugins(EtcdUtil.getSimpleName(path), plugins);

    }
    public boolean removeListenerIfNeeded() {
        return true;
    }
}
