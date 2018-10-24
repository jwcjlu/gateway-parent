package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum MatchModeEnum {

    /**
     * And match mode enum.
     */
    AND(0, "and"),

    /**
     * Or match mode enum.
     */
    OR(1, "or");

    private final int code;

    private final String name;
}
