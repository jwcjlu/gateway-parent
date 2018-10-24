package com.jwcjlu.gateway.sdk.http;

public class HeaderBuilder{
    private String serverKey;
    private String protocol;
    private String sign;
    private String appKey;
    private String module;
    private String interfaceName;
    private String timestamp;
    public HeaderBuilder serverKey(String serverKey){
        this.serverKey=serverKey;
        return this;
    }
    public HeaderBuilder protocol(String protocol){
        this.protocol=protocol;
            return this;
    }
    public HeaderBuilder sign(String sign){
        this.sign=sign;
        return this;
    }
    public HeaderBuilder appKey(String appKey){
        this.appKey=appKey;
       return this;
    }
    public HeaderBuilder module(String module){
        this.module=module;
        return this;
    }
    public HeaderBuilder interfaceName(String interfaceName){
        this.interfaceName=interfaceName;
        return this;
    }
    public HeaderBuilder timestamp(String timestamp){
        this.timestamp=timestamp;
        return this;
    }

    public Header build(){
        return new Header(serverKey,protocol,sign,appKey,module,interfaceName,timestamp);
    }

    public HeaderBuilder(String protocol) {
        this.protocol = protocol;
    }
}
