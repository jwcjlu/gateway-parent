package com.jwcjlu.gateway.core.lb;


import com.jwcjlu.gateway.core.node.ServerInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    public AbstractLoadBalance(){
        register();
    }

    @Override
    public ServerInfo select(List<ServerInfo> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        return doSelect(servers);
    }

    protected abstract ServerInfo doSelect(List<ServerInfo> servers);

    protected void register() {
        LoadBalanceFactory.INSTANCE.registerLoadBalace(name(), this);
    }
}
