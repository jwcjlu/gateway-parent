package com.jwcjlu.gateway.httpServer.exception;

public class RefuseTimeoutRequestException extends  Throwable {
    public RefuseTimeoutRequestException(String msg){
        super(msg);
    }
}
