package com.jwcjlu.gateway.httpServer.adapter;

import com.jwcjlu.gateway.common.boot.BootService;

import java.util.Properties;

public abstract  class BootServiceAdapter implements BootService {
    @Override
    public void initialize(Properties properties) throws Exception{

    }

    @Override
    public void boot() {

    }
    public  void onComplete(){

    }
}
