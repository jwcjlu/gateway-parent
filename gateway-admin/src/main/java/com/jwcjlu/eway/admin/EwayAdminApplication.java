package com.jwcjlu.gateway.admin;

import com.jwcjlu.gateway.configuration.redis.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * run class.
 * @author xiaoyu
 */
@SpringBootApplication
@ComponentScan("com.jwcjlu")
@Import(value = { RedisConfiguration.class})
public class EwayAdminApplication {

    /**
     * run class.
     */
    public static void main(String[] args) {
        SpringApplication.run(EwayAdminApplication.class, args);
    }


}
