package com.jwcjlu.gateway.httpServer.etcd.listener.rule;

import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractChildListener;

import java.util.List;

public class RuleServiceChildListener extends AbstractChildListener {


    @Override
    public void handlerChildChanged(String path, List<String> children) {
        children.forEach(c -> {
            etcdService.addDataListener(c, new RuleServiceDataListener());
        });
    }

    @Override
    public boolean removeListenerIfNeeded() {
        return true;
    }
}