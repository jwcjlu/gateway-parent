package com.jwcjlu.gateway.core.etcd.mock.lb;

import com.jwcjlu.gateway.core.lb.LoadBalance;
import com.jwcjlu.gateway.core.lb.LoadBalanceFactory;
import com.jwcjlu.gateway.core.node.ServerInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LoadBalanceTest {
    @Test
    public void test() {
        LoadBalance lb = LoadBalanceFactory.INSTANCE.getDefaultLoadBalance();
        List<ServerInfo> serverInfoList = new ArrayList<>();
        serverInfoList.add(new ServerInfo("120.21414", 212,100));
        serverInfoList.add(new ServerInfo("120.21414", 2123,100));
        for(int i = 0; i< 30; i++){
            System.out.println(lb.select(serverInfoList));
        }
        System.out.println("======================================");
        serverInfoList.get(0).setWeight(2);
        serverInfoList.get(1).setWeight(1);
        for(int i = 0; i< 30; i++){
            System.out.println(lb.select(serverInfoList));
        }

    }
}
