package com.jwcjlu.gateway.httpServer.handler;


import com.jwcjlu.gateway.api.dto.etcd.PluginEtcdDTO;
import com.jwcjlu.gateway.common.boot.BootServiceManager;
import com.jwcjlu.gateway.common.constant.Constants;
import com.jwcjlu.gateway.common.enums.RpcTypeEnum;
import com.jwcjlu.gateway.httpServer.DTO.RequestDTO;
import com.jwcjlu.gateway.httpServer.adapter.BootServiceAdapter;
import com.jwcjlu.gateway.httpServer.cache.DataCacheManager;
import com.jwcjlu.gateway.httpServer.exception.NotFoundPluginExcption;
import com.jwcjlu.gateway.httpServer.exception.RequestParameterException;
import com.jwcjlu.gateway.httpServer.filter.DefaultFilterChain;
import com.jwcjlu.gateway.httpServer.filter.FilterType;
import com.jwcjlu.gateway.httpServer.filter.HttpFilter;
import com.jwcjlu.gateway.httpServer.filter.HttpFilters;
import com.jwcjlu.gateway.httpServer.netty.util.HttpUtils;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultHttpHandler extends BootServiceAdapter implements HttpHandler {
    private Logger logger = LoggerFactory.getLogger(DefaultHttpHandler.class);
    private DataCacheManager dataCacheManager;
    private long DEFAULT_REQUEST_REFUSE_TIME = 60 * 10 * 1000;

    @Override
    public void dispatcherHandler(HttpHandlerContent requestContent) {
        try {

            RequestDTO dto = RequestDTO.build(requestContent.getRequest());
            logger.info(buildRequestLogInfo(requestContent.getRequest(), dto));
            validate(dto, requestContent);
            requestContent.attribute(Constants.REQUESTDTO, dto);
            List<PluginEtcdDTO> plugins = dataCacheManager.getPlugins(dto.getServerKey());
            if (CollectionUtils.isEmpty(plugins)) {
                logger.error("not found any plugin");
                throw new NotFoundPluginExcption("not found any plugin");
            }
            List<HttpFilter> filters = plugins.stream().map(plugin -> {
                return HttpFilters.INSTANCE.getHttpFilter(plugin.getName());
            }).collect(Collectors.toList());
            new DefaultFilterChain(filters, Arrays.asList(FilterType.PRE, FilterType.ROUTING))
                .doFilter(requestContent);
        } catch (Throwable t) {
            logger.error("requst failure!!!" + t);
            if (requestContent.getResponse() != null) {
                requestContent.response();
            } else {
                requestContent.response(HttpUtils.INSTANCE
                    .createFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY, t.getMessage()));
            }
        }
    }

    @Override
    public void onComplete() {
        dataCacheManager = BootServiceManager.INSTANCE.findService(DataCacheManager.class);
    }

    @Override
    public void shutdown() {

    }

    private void validate(final RequestDTO requestDTO, HttpHandlerContent requestContent) throws Throwable {
        if (Objects.isNull(requestDTO)
            || StringUtils.isBlank(requestDTO.getTimestamp())
            || StringUtils.isBlank(requestDTO.getModule())
            || StringUtils.isBlank(requestDTO.getServerKey())) {
            throw new RequestParameterException("timestamp 、module、serverKey field is required ");
        }
        final RpcTypeEnum rpcTypeEnum = RpcTypeEnum.buildByName(requestDTO.getProtocol());
        if (Objects.isNull(rpcTypeEnum)) {
            throw new RequestParameterException("protocol is error，Protocol must be http, dubbo");
        }
       /* if (System.currentTimeMillis() - Long.parseLong(requestDTO.getTimestamp()) > DEFAULT_REQUEST_REFUSE_TIME) {
            requestContent.setResponse(HttpUtils.INSTANCE
                .createFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.GATEWAY_TIMEOUT, "reject requests for more than ten minutes"));
            throw new RefuseTimeoutRequestException("reject requests for more than ten minutes");
        }*/
    }

    private String buildRequestLogInfo(HttpRequest request, RequestDTO dto) {
        StringBuilder sb = new StringBuilder();
        sb.append(dto.toString());
        sb.append(" path:").append(request.uri()).append(" method:").append(request.method());
        return sb.toString();

    }

}
