package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Param Type.
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum ParamTypeEnum {

    /**
     * Post param type enum.
     */
    POST("post"),

    /**
     * Uri param type enum.
     */
    URI("uri"),

    /**
     * Query param type enum.
     */
    QUERY("query"),

    /**
     * Host param type enum.
     */
    HOST("host"),

    /**
     * Ip param type enum.
     */
    IP("ip"),

    /**
     * Header param type enum.
     */
    HEADER("header");


    private final String name;

}
