package com.jwcjlu.gateway.httpServer.etcd.listener.service;


import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractChildListener;

import java.util.List;

public class ServiceChildListener extends AbstractChildListener {

    @Override
    public void handlerChildChanged(String path, List<String> children) {
        children.forEach(c -> {
            etcdService.addChildListener(c, new ServicePluginChildListener());
        });

    }
}
