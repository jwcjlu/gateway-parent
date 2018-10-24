package com.jwcjlu.gateway.httpServer.etcd.listener;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.util.StringUtil;
import com.jwcjlu.gateway.core.etcd.DataListener;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import com.jwcjlu.gateway.httpServer.etcd.EtcdService;

public abstract  class AbstractDataListener implements DataListener{
    protected DataCacheManager dataCacheManager;
    protected EtcdService etcdService;
    public AbstractDataListener(){
        this.etcdService= BootServiceManager.INSTANCE.findService(EtcdService.class);
        dataCacheManager= BootServiceManager.INSTANCE.findService(DataCacheManager.class);
    }
    @Override
    public void handleDataChange(String path, String data) {
        if(StringUtil.isEmpty(data)){
            etcdService.removeDataListener(path,this);
        }else {
            handleData(path,data);
        }

    }
    public  abstract void handleData(String path, String data);
}
