package com.jwcjlu.gateway.httpServer.etcd;

import com.jwcjlu.gateway.common.boot.BootService;
import com.jwcjlu.gateway.common.boot.DefaultImplementor;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.ConfigUtils;
import com.jwcjlu.gateway.core.etcd.ChildListener;
import com.jwcjlu.gateway.core.etcd.DataListener;
import com.jwcjlu.gateway.core.etcd.EtcdClient;
import com.jwcjlu.gateway.core.etcd.StateListener;
import com.jwcjlu.gateway.core.etcd.support.JEtcdClient;

import java.util.List;
import java.util.Properties;

@DefaultImplementor
public class EtcdService implements BootService {
    private String endPoints;
    private EtcdClient etcdClient;

    @Override
    public void initialize(Properties properties) {
        endPoints = ConfigUtils.getString(properties, Constants.ETCD_ENDPOINTS);
    }

    @Override
    public void boot() {
        etcdClient = new JEtcdClient(endPoints);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void shutdown() {
        etcdClient.close();
    }

    public long createEphemeral(String path) {
        return etcdClient.createEphemeral(path);
    }

    public List<String> getChildren(String path) {
        return etcdClient.getChildren(path);
    }


    public List<String> addChildListener(String path, ChildListener listener) {
        return etcdClient.addChildListener(path, listener);
    }

    public void removeChildListener(String path, ChildListener listener) {
        etcdClient.removeChildListener(path, listener);
    }

    public String addDataListener(String path, DataListener listener) {
        return etcdClient.addDataListener(path, listener);
    }

    public void removeDataListener(String path, DataListener listener) {
        etcdClient.removeDataListener(path, listener);
    }

    public void addStateListener(StateListener stateListener) {
        etcdClient.addStateListener(stateListener);
    }

    public void removeStateListener(StateListener stateListener) {
        etcdClient.removeStateListener(stateListener);
    }

    public void removeDataListener(String path) {
        etcdClient.removeDataListener(path);
    }

    public void removeChildListener(String path) {
        etcdClient.removeChildListener(path);
    }

    public boolean checkExist(String path) {
        return etcdClient.isExist(path);
    }


}
