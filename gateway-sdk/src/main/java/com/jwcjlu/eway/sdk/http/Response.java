package com.jwcjlu.gateway.sdk.http;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;

import java.io.Serializable;
import java.nio.charset.Charset;

public class Response implements Serializable{
    private String context;
    private int code;
    private boolean isSuccess=false;
    private HttpHeaders httpHeaders;
    private HttpVersion version;

    public Response(String context, int code, boolean isSuccess) {
        this.context = context;
        this.code = code;
        this.isSuccess = isSuccess;
        this.version = HttpVersion.HTTP_1_1;
    }

    public Response(HttpResponse response, int code) {
        this.code = code;
        if(response instanceof FullHttpResponse) {
            FullHttpResponse res=(FullHttpResponse)response;
            if (res.content() != null && res.content().capacity() > 0) {
                byte[] byteArray = new byte[res.content().capacity()];
                res.content().readBytes(byteArray);
                context = new String(byteArray, Charset.defaultCharset());
            }
            httpHeaders=res.headers();
            version=res.protocolVersion();
        }
        if(code<300){
            isSuccess=true;
        }
    }
    public Response() {

    }

    public static Response toResponse(HttpResponse resp){
       int code= resp.status().code();
        return new Response(resp,code);
    }

    public String getContext() {
        return context;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public HttpVersion getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Response{" +
            "context='" + context + '\'' +
            ", code=" + code +
            ", isSuccess=" + isSuccess +
            ", httpHeaders=" + httpHeaders +
            ", version=" + version +
            '}';
    }
}
