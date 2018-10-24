package com.jwcjlu.gateway.sdk.service;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.util.EtcdUtil;
import com.jwcjlu.gateway.core.etcd.ChildListener;
import com.jwcjlu.gateway.core.etcd.EtcdClient;
import com.jwcjlu.gateway.core.etcd.support.JEtcdClient;
import com.jwcjlu.gateway.core.node.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public  class ServiceDiscovery {
    private String endPoints;
    private Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
    public static List<ServerInfo> serverInfoList=new ArrayList<ServerInfo>();
    private EtcdClient etcdClient;
    public ServiceDiscovery(String endPoints) {
        this.endPoints = endPoints;
    }
    public void start() {
        etcdClient = new JEtcdClient(endPoints);
        List<String> childrens = etcdClient.addChildListener(Constants.SERVER_PATH, new ChildListener() {
            @Override
            public void childChanged(String path, List<String> children) {
                parseAndAssign(children);
            }
        });
        parseAndAssign(childrens);

    }

    private void parseAndAssign(List<String> children) {
        logger.debug("gateway service node changes");
        if (Objects.isNull(children)) {
            return;
        }
        List<ServerInfo> serverInfos = new ArrayList<ServerInfo>();
        for (String hostAndPort : children) {
            try {
                String[] hostPorts = EtcdUtil.getSimpleName(hostAndPort).split(Constants.HOST_PORT_SEPARATE);
                ServerInfo serverInfo = new ServerInfo(hostPorts[0], Integer.parseInt(hostPorts[1]));
                serverInfos.add(serverInfo);
                serverInfoList=serverInfos;
            } catch (Throwable e) {
                logger.error("{} paser failure!!!" + hostAndPort);
            }
        }

    }

    public void shutdown() {
        etcdClient.close();
    }



}
