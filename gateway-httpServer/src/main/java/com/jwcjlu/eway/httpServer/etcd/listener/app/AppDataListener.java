package com.jwcjlu.gateway.httpServer.etcd.listener.app;

import com.jwcjlu.gateway.api.dto.etcd.AppAuthEtcdDTO;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.common.util.JsonUtil;
import com.jwcjlu.gateway.httpServer.etcd.listener.AbstractDataListener;

public class AppDataListener extends AbstractDataListener {


    @Override
    public void handleData(String path, String data) {
        dataCacheManager.putApps(EtcdUtil.getSimpleName(path), JsonUtil.fromJson(data, AppAuthEtcdDTO.class));
    }
}
