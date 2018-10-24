package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>Description: .</p>
 * 参数类型
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum OperatorEnum {

    /**
     * Match operator enum.
     */
    MATCH("match"),

    /**
     * Eq operator enum.
     */
    EQ("="),

    /**
     * Gt operator enum.
     */
    GT(">"),

    /**
     * Lt operator enum.
     */
    LT("<"),

    /**
     * Like operator enum.
     */
    LIKE("like");

    private final String alias;


}
