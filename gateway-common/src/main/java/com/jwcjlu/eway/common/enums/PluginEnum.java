package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PluginEnum.
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum PluginEnum {

    /**
     * Global plugin enum.
     */
    GLOBAL(1, "global"),


    /**
     * Sign plugin enum.
     */
    SIGN(2, "sign"),

    /**
     * Waf plugin enum.
     */
    WAF(10, "waf"),

    /**
     * Rate limiter plugin enum.
     */
    CONCURRENCY(20, "concurrency"),

    /**
     * Rewrite plugin enum.
     */
    REWRITE(30, "rewrite"),

    /**
     * Redirect plugin enum.
     */
    REDIRECT(40, "redirect"),

    /**
     * Divide plugin enum.
     */
    DIVIDE(50, "divide"),

    /**
     * Dubbo plugin enum.
     */
    DUBBO(60, "dubbo"),

    /**
     * Monitor plugin enum.
     */
    MONITOR(70, "monitor");

    private final int code;

    private final String name;


}
