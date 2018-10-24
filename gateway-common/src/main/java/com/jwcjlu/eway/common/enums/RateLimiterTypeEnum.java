package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * WafEnum.
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum RateLimiterTypeEnum {

    /**
     * redis  to rateLimiter .
     */
    TYPE_REDIS(0, "redis限流"),

    /**
     * count to rateLimiter .
     */
    TYPE_(1, "本地计数");

    private final int code;

    private final String name;
}
