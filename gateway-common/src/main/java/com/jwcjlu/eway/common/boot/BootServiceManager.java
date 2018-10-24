package com.jwcjlu.gateway.common.boot;


import com.jwcjlu.gateway.common.exception.ServiceConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BootService management, including initialization, startup, shutdown,
 * and so on. Find the response to the bootService
 */
public enum BootServiceManager {
    INSTANCE;
    private boolean boot = false;
    private Logger logger = LoggerFactory.getLogger(BootServiceManager.class);
    private Map<Class, BootService> bootedServices = new ConcurrentHashMap<Class, BootService>();

    public void boot(Properties properties) {
        bootedServices = loadAllServices();
        initialize(properties);
        boot();
        onComplete();

    }

    public void shutdown() {
        for (BootService bootService : bootedServices.values()) {
            try {
                bootService.shutdown();
            } catch (Throwable e) {
                logger.error(String.format("BootServiceManager try to shutdown [{}] fail.", bootService.getClass().getName()) + e);
            }
        }
    }

    private Map<Class, BootService> loadAllServices() {
        Map<Class, BootService> bootedServices = new LinkedHashMap<Class, BootService>();
        List<BootService> allServices = new LinkedList<BootService>();
        load(allServices);
        Iterator<BootService> serviceIterator = allServices.iterator();
        while (serviceIterator.hasNext()) {
            BootService bootService = serviceIterator.next();

            Class<? extends BootService> bootServiceClass = bootService.getClass();
            boolean isDefaultImplementor = bootServiceClass.isAnnotationPresent(DefaultImplementor.class);
            if (isDefaultImplementor) {
                if (!bootedServices.containsKey(bootServiceClass)) {
                    bootedServices.put(bootServiceClass, bootService);
                } else {
                    //ignore the default service
                }
            } else {
                OverrideImplementor overrideImplementor = bootServiceClass.getAnnotation(OverrideImplementor.class);
                if (overrideImplementor == null) {
                    if (!bootedServices.containsKey(bootServiceClass)) {
                        bootedServices.put(bootServiceClass, bootService);
                    } else {
                        throw new ServiceConflictException("Duplicate service define for :" + bootServiceClass);
                    }
                } else {
                    Class<? extends BootService> targetService = overrideImplementor.value();
                    if (bootedServices.containsKey(targetService)) {
                        boolean presentDefault = bootedServices.get(targetService).getClass().isAnnotationPresent(DefaultImplementor.class);
                        if (presentDefault) {
                            bootedServices.put(targetService, bootService);
                        } else {
                            throw new ServiceConflictException("Service " + bootServiceClass + " overrides conflict, " +
                                "exist more than one service want to override :" + targetService);
                        }
                    } else {
                        bootedServices.put(targetService, bootService);
                    }
                }
            }

        }
        return bootedServices;
    }

    public <T extends BootService> T findService(Class<T> serviceType) {
        return (T) bootedServices.get(serviceType);
    }

    private void load(List<BootService> allServices) {
        Iterator<BootService> iterator = ServiceLoader.load(BootService.class).iterator();
        while (iterator.hasNext()) {
            allServices.add(iterator.next());
        }
    }

    private void initialize(Properties properties) {
        logger.info("initialize bootService start==============================");
        for (BootService bootService : bootedServices.values()) {
            try {
                bootService.initialize(properties);
                logger.info(bootService.getClass() + " Successful initialization ");
            } catch (Throwable e) {
                logger.error(String.format("BootServiceManager try to initialize [{}] fail.", bootService.getClass().getName()) + e);
            }

        }
        logger.info("initialize bootService end=============================");
    }

    private void boot() {
        logger.info("boot bootService start==============================");
        for (BootService bootService : bootedServices.values()) {
            try {
                logger.info("component "+bootService.getClass() + " Successful  boot");
                bootService.boot();
            } catch (Throwable e) {
                logger.error(String.format("BootServiceManager try to boot [{}] fail.", bootService.getClass().getName()) + e);
            }
        }
        logger.info("boot bootService end=============================");

    }

    private void onComplete() {
        for (BootService bootService : bootedServices.values()) {
            try {
                bootService.onComplete();
            } catch (Throwable e) {
                logger.error(String.format("BootServiceManager try to onComplete [{}] fail.", bootService.getClass().getName()) + e);
            }
        }

    }
}
