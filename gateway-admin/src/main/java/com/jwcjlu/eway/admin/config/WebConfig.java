package com.jwcjlu.gateway.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * web handler.
 *
 * @author xiaoyu
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(final CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedHeaders("Access-Control-Allow-Origin", "*", "Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE",
                        "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        // Add more mappings...
    }
}
