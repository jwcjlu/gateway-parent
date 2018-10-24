package com.jwcjlu.gateway.core.etcd;


public interface DataListener extends Listener {

    void handleDataChange(String path, String data);
}
