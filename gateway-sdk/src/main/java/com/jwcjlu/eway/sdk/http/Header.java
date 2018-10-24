package com.jwcjlu.gateway.sdk.http;

import com.jwcjlu.gateway.common.util.ReflectUtil;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class Header implements Serializable {
    private String serverKey;
    private String protocol;
    private String sign;
    private String appKey;
    private String module;
    private String interfaceName;
    private String timestamp;
    private Map<String, Object> header = new HashMap<String, Object>();

    public Header(String serverKey, String protocol, String sign, String appKey, String module, String interfaceName, String timestamp) {
        this.serverKey = serverKey;
        this.protocol = protocol;
        this.sign = sign;
        this.appKey = appKey;
        this.module = module;
        this.interfaceName = interfaceName;
        this.timestamp = timestamp;
    }

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

    public void addHeaders(String key, Object value) {
        header.put(key, value);
    }

    public HttpHeaders toHttpHeaders( HttpHeaders headers) {
        Field[] fields = ReflectUtil.getAllField(this.getClass());
        for (Field field : fields) {
            if(field.getName().equals("header")){
                continue;
            }
            Object value = ReflectUtil.getValueByField(field, this);
            if (!Objects.isNull(value)) {
                headers.add(field.getName(), value);
            }
        }
        for (String key :
            header.keySet()) {
            headers.add(key, header.get(key));
        }
        return headers;
    }
    public static HeaderBuilder http(){
        return new HeaderBuilder("http");
    }
}
