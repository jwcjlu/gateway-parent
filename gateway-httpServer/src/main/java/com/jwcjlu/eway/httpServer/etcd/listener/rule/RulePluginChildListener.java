package com.jwcjlu.gateway.httpServer.etcd.listener.rule;


import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractChildListener;

import java.util.List;

public class RulePluginChildListener extends AbstractChildListener {


    @Override
    public void handlerChildChanged(String path, List<String> children) {
        children.forEach(c -> {
            etcdService.addChildListener(c, new RuleServiceChildListener());
        });

    }

}
