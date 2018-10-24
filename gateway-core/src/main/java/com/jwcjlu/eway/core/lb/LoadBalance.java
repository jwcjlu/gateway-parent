package com.jwcjlu.gateway.core.lb;


import com.jwcjlu.gateway.core.node.ServerInfo;

import java.util.List;

public interface LoadBalance {
    ServerInfo select(List<ServerInfo> servers);

    String name();
}
