package com.jwcjlu.gateway.netty.http.server;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Random;

public class BigObjFactory implements PooledObjectFactory<BigObj> {
    @Override
    public PooledObject<BigObj> makeObject() throws Exception {
        return new DefaultPooledObject<BigObj>(new BigObj());
    }

    @Override
    public void destroyObject(PooledObject<BigObj> pooledObject)
        throws Exception {
        pooledObject.getObject().destroy();
    }

    @Override
    public boolean validateObject(PooledObject<BigObj> pooledObject) {
        //默认是false,代表验证不通过,这里随机好了
        return new Random().nextInt()%2==0;
    }

    @Override
    public void activateObject(PooledObject<BigObj> pooledObject)
        throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<BigObj> pooledObject)
        throws Exception {
    }


}
