package com.jwcjlu.gateway.sdk.service.support;

import com.jwcjlu.gateway.sdk.service.ServiceDiscovery;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PreDestroy;

public class SpringServiceDiscovery extends ServiceDiscovery implements InitializingBean {
    public SpringServiceDiscovery(String endPoints) {
        super(endPoints);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
    @PreDestroy
    public void  shutdown(){
       super.shutdown();
    }
}
