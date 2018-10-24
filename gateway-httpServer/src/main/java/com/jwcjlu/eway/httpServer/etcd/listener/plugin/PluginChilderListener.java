package com.jwcjlu.gateway.httpServer.etcd.listener.plugin;
import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractChildListener;
import java.util.List;

public class PluginChilderListener extends AbstractChildListener {


    @Override
    public void handlerChildChanged(String path, List<String> children) {
        children.forEach((c) -> {
          etcdService.addDataListener(c, new PluginDataListener());

        });
    }
}
