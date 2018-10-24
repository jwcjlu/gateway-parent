package com.jwcjlu.gateway.httpServer.adapter;

import com.jwcjlu.gateway.common.boot.BootService;

import java.io.IOException;

public abstract class InitializeBootServiceAdapter implements BootService{
    @Override
    public void boot() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void shutdown() throws IOException {

    }
}
