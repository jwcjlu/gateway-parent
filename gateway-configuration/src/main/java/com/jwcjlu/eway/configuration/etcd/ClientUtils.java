package com.jwcjlu.gateway.configuration.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-30 11:11
 **/
public class ClientUtils {

    //@Value("${etcdUrl}")
    private static String etcdUrl;

    private static Object obj = new Object();

    private static Client client;

    private static KV kvClient;

    public static KV getInstence() {
        if(kvClient == null){
            synchronized (obj){
                if(kvClient == null){
                    client = Client.builder().endpoints("http://10.60.38.51:2379").build();
                    kvClient = client.getKVClient();
                }
            }
        }
        return kvClient;
    }
}
