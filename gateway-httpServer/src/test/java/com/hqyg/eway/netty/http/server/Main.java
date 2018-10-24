package com.jwcjlu.gateway.netty.http.server;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class Main {
    public static void main(String[] args) throws Exception {
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(5);
        conf.setTestOnReturn(true);
        GenericObjectPool<BigObj> pool = new GenericObjectPool<BigObj>(new BigObjFactory(), conf);
        for(int i =0;i< 10; i++) {
            BigObj bigObj = pool.borrowObject();
            System.out.println(i + " time get " + bigObj.getV());
            if((i+1)%3==0)
            pool.returnObject(bigObj);
        }
    }

}
