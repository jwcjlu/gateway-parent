package com.jwcjlu.gateway.httpServer.etcd.listener;

import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.core.etcd.ChildListener;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import com.jwcjlu.gateway.httpServer.etcd.EtcdService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract  class AbstractChildListener implements ChildListener {
    protected DataCacheManager dataCacheManager;
    protected List<String> oldChilders;
    protected EtcdService etcdService;
    public AbstractChildListener(){
        this.etcdService=BootServiceManager.INSTANCE.findService(EtcdService.class);
        dataCacheManager= BootServiceManager.INSTANCE.findService(DataCacheManager.class);
    }
    @Override
    public void childChanged(String path, List<String> children) {
        if(CollectionUtils.isEmpty(children)&&removeListenerIfNeeded()){
            etcdService.removeChildListener(path,this);
        }
        handlerChildChanged(path,children);

    }
    public abstract void handlerChildChanged(String path, List<String> children);
    public boolean removeListenerIfNeeded(){
        return false;
    }


}
