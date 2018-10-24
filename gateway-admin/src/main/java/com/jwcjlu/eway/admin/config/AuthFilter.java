package com.jwcjlu.gateway.admin.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 */
@Component
public class AuthFilter implements WebFilter {

    private static final String DEV = "dev";

    private static final String LOCAL = "local";

    private final HashSet<String> excludePaths =
            Sets.newHashSet("/index.html");

    private final HashSet<String> tokenPaths =
            Sets.newHashSet("/gateway/role","/gateway/service", "/gateway/rule", "/gateway/user", "/gateway/menu", "/gateway/appAuth",
                    "/gateway/plugin");

    private static final HashSet<String> passPaths =
            Sets.newHashSet("/gateway/plugin/getPluginStatus", "/gateway/plugin/changePluginStatus", "/gateway/enum/getEnum",
                    "/gateway/selector/conditionList", "/gateway/rule/conditionList","/gateway/service/getServiceListForRule", "/gateway/rule/ruleList", "/gateway/role/roleList");


    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public Mono<Void> filter(final ServerWebExchange serverWebExchange, final WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        final String path = request.getPath().pathWithinApplication().value();
        Iterator iterator = excludePaths.iterator();
        if (iterator.hasNext()) {
            if (iterator.next().equals(path)) {
                if (env.equals(DEV)) {
                    return webFilterChain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest().mutate().path("/index-dev.html").build()).build());
                } else if (env.equals(LOCAL)) {
                    return webFilterChain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest().mutate().path("/index-local.html").build()).build());
                } else {
                    return webFilterChain.filter(serverWebExchange.mutate().request(serverWebExchange.getRequest().mutate().path("/index-prod.html").build()).build());
                }
            }
        }

        /*if (!isPassCompetence(path)) {
            Iterator tokenIterator = tokenPaths.iterator();
            while (tokenIterator.hasNext()) {
                if (path.contains(tokenIterator.next().toString())) {
                    List<String> token = request.getHeaders().get("token");
                    if (Objects.nonNull(token) && StringUtils.isNotEmpty(token.get(0))) {
                        Object role = redisTemplate.opsForValue().get(token.get(0));
                        if (Objects.nonNull(role)) {
                            String authorize = redisTemplate.opsForValue().get(role + "_authorize").toString();
                            JSONObject json = JSONObject.parseObject(authorize);
                            if (Objects.isNull(json.get(path))) {
                                response.setStatusCode(HttpStatus.FORBIDDEN);
                                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                return response.writeWith(Mono.just(response.bufferFactory().wrap("{\"message\":\"无此权限\"}".getBytes())));
                            }
                        }
                    } else {
                        response.setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        return response.writeWith(Mono.just(response.bufferFactory().wrap("{\"message\":\"请先登录\"}".getBytes())));
                    }
                }
            }
        }*/
        return webFilterChain.filter(serverWebExchange);
    }

    /**
     * if is in pass list.
     *
     * @param path request path
     * @return java.lang.Boolean
     */
    private static Boolean isPassCompetence(final String path) {
        Iterator passIterator = passPaths.iterator();
        while (passIterator.hasNext()) {
            if (passIterator.next().equals(path)) {
                return true;
            }
        }
        return false;
    }
}