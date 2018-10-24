package com.jwcjlu.gateway.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * HttpServerConfig.
 * @author chengchuantuo
 */
@Configuration
public class HttpServerConfig {

    private final Environment environment;

    @Autowired
    public HttpServerConfig(final Environment environment) {
        this.environment = environment;
    }

}
