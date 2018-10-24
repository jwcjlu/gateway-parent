package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 支持的轮训枚举.
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum LoadBalanceEnum {

    /**
     * Hash load balance enum.
     */
    HASH(1, "hash"),

    /**
     * Random load balance enum.
     */
    RANDOM(2, "random"),

    /**
     * Round robin load balance enum.
     */
    ROUND_ROBIN(3, "roundRobin");

    private final int code;

    private final String name;

}
