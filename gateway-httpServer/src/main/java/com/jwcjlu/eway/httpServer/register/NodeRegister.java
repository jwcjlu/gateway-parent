package com.jwcjlu.gateway.httpServer.register;

import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.boot.DefaultImplementor;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.ConfigUtils;
import com.jwcjlu.gateway.core.etcd.StateListener;
import com.jwcjlu.gateway.httpServer.adapter.BootServiceAdapter;
import com.jwcjlu.gateway.httpServer.etcd.EtcdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@DefaultImplementor
public class NodeRegister extends BootServiceAdapter {
    private Logger logger = LoggerFactory.getLogger(NodeRegister.class);
    private String host;
    private int port;

    @Override
    public void initialize(Properties properties) {
        host = ConfigUtils.getString(properties, "gateway.register.host");
        port = ConfigUtils.getInt(properties, "gateway.port");

    }

    @Override
    public void onComplete() {
        String ephemeralNode = Constants.SERVER_PATH + Constants.PATH_SEPARATOR + host + Constants.HOST_PORT_SEPARATE + port;
        EtcdService etcdService = BootServiceManager.INSTANCE.findService(EtcdService.class);
        etcdService.createEphemeral(ephemeralNode);
        etcdService.addStateListener((state) -> {
            if (StateListener.CONNECTED == state) {
                boolean flag = true;
                while (flag) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        if (etcdService.checkExist(ephemeralNode)) {
                            flag=false;
                            return;
                        }
                        etcdService.createEphemeral(ephemeralNode);
                        logger.info("node register ");
                        flag=false;
                    } catch (Exception e) {
                        logger.error("register service failure！！", e);
                    }
                }
            }
        });
    }

    @Override
    public void shutdown() {

    }

}
