package com.jwcjlu.gateway.common.exception;

public class ServiceConflictException  extends RuntimeException {
    public ServiceConflictException(String message) {
        super(message);
    }
}