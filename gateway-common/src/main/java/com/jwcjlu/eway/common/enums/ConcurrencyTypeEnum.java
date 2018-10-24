package com.jwcjlu.gateway.common.enums;

public enum ConcurrencyTypeEnum {
    redis(0),
    local(1);

    private ConcurrencyTypeEnum(int value) {
        this.value = value;
    }

    private int value;
    public int getValue(){
        return value;
    }
}
