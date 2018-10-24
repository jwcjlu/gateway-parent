package com.jwcjlu.gateway.common.boot;

import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public interface BootService {
    void initialize(Properties properties) throws Throwable;

    void boot();

    void onComplete();

    void shutdown() throws IOException;
}
