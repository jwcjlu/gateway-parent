package com.jwcjlu.gateway.httpServer.etcd.listener.plugin;

import com.jwcjlu.gateway.api.dto.etcd.PluginEtcdDTO;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.common.util.JsonUtil;
import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractDataListener;

public class PluginDataListener extends AbstractDataListener {

    @Override
    public void handleData(String path, String data) {
        dataCacheManager.putPlugins(EtcdUtil.getSimpleName(path), JsonUtil.fromJson(data, PluginEtcdDTO.class));
    }
}
