package com.jwcjlu.gateway.httpServer.DTO;

import com.jwcjlu.gateway.common.util.StringUtil;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

public class RequestDTO implements Serializable {
    private Logger logger= LoggerFactory.getLogger(RequestDTO.class);
    private String serverKey;
    private String protocol;
    private String sign;
    private String appKey;
    private String module;
    private String interfaceName;
    private String timestamp;
    private String method;

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
            "serverKey='" + serverKey + '\'' +
            ", protocol='" + protocol + '\'' +
            ", sign='" + sign + '\'' +
            ", appKey='" + appKey + '\'' +
            ", module='" + module + '\'' +
            ", interfaceName='" + interfaceName + '\'' +
            ", timestamp='" + timestamp + '\'' +
            ", method='" + method + '\'' +
            '}';
    }

    public static RequestDTO build(HttpRequest httpRequest) {
        RequestDTO requestDTO = new RequestDTO();
        Field[] fields = RequestDTO.class.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            String value = httpRequest.headers().get(field.getName());
            if (StringUtil.isNotEmpty(value)) {
                try {
                    field.set(requestDTO, value);
                } catch (IllegalAccessException e) {
                }
            }

        });
        return requestDTO;
    }
}
