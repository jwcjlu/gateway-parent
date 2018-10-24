package com.jwcjlu.gateway.httpServer.filter.support;

import com.jwcjlu.gateway.api.convert.DivideHandle;
import com.jwcjlu.gateway.api.convert.Handler;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.enums.RpcTypeEnum;
import com.jwcjlu.gateway.core.lb.LoadBalanceFactory;
import com.jwcjlu.gateway.core.node.ServerInfo;
import com.jwcjlu.gateway.httpServer.DTO.RequestDTO;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.exception.FilterException;
import com.jwcjlu.gateway.httpServer.filter.AbstractHttpFilter;
import com.jwcjlu.gateway.httpServer.filter.FilterChain;
import com.jwcjlu.gateway.httpServer.filter.FilterType;
import com.jwcjlu.gateway.httpServer.netty.connection.ProxyToServerConnection;
import com.jwcjlu.gateway.httpServer.netty.connectionpool.ConnectionPoolManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DivideFilter extends AbstractHttpFilter {
    private Logger logger = LoggerFactory.getLogger(DivideFilter.class);

    @Override
    public String name() {
        return "divide";
    }

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.ROUTING;
    }

    @Override
    public void doFilter(HttpHandlerContent content, FilterChain chain) throws Throwable {
        RequestDTO requestDTO = content.getAttribute(Constants.REQUESTDTO);
        final RpcTypeEnum rpcTypeEnum = RpcTypeEnum.buildByName(requestDTO.getProtocol());
        if (rpcTypeEnum != RpcTypeEnum.HTTP) {
            return;
        }
        DivideHandle handle = BootServiceManager.INSTANCE.findService(DataCacheManager.class)
            .getHandler(name(), requestDTO.getServerKey());
        if (Objects.isNull(handle)) {
            throw new FilterException("No rule found, processing failed");
        }
        if (CollectionUtils.isEmpty(handle.getUpstreamList())) {
            throw new FilterException("No upstream found, processing failed");
        }
        List<ServerInfo> serverInfoList = handle.getUpstreamList().stream().map(upstream -> {
            return new ServerInfo(upstream.getUpstreamHost(), upstream.getUpstreamPort(),
                upstream.getWeight(), upstream.getRetry(), upstream.getTimeout());
        }).collect(Collectors.toList());
        Throwable e1 = null;
        for (int i = 0; i < serverInfoList.size(); i++) {
            try {
                ServerInfo serverInfo = LoadBalanceFactory.INSTANCE.getLoadBalance(handle.getLoadBalance()).select(serverInfoList);
                ProxyToServerConnection proxyToServerConnection = ConnectionPoolManager.INSTANCE.getConnectionPool(serverInfo)
                    .borrowObject(content, serverInfo.getTimeout());
                proxyToServerConnection.writeToChannel(content.getRequest());
                content.attribute(Constants.REAL_SERVER, serverInfo);
                content.attribute(Constants.START_TIME, new Long(System.currentTimeMillis()));
                return;
            } catch (Throwable e) {
                logger.error("route error! ", e);
                e1 = e;
            }
        }
        if (e1 != null) {
            throw e1;
        }

    }

    public Class<? extends Handler> handlerClass() {
        return DivideHandle.class;
    }
}
