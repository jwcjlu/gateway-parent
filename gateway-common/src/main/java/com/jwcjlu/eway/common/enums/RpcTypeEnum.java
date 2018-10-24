package com.jwcjlu.gateway.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum RpcTypeEnum {

    /**
     * Http rpc type enum.
     */
    HTTP("http"),

    /**
     * Dubbo rpc type enum.
     */
    DUBBO("dubbo"),;

    private final String name;

    /**
     * 封装.
     *
     * @param name 名称
     * @return RpcTypeEnum
     */
    public static RpcTypeEnum buildByName(final String name) {
        return Arrays.stream(RpcTypeEnum.values())
                .filter(rpcTypeEnum -> Objects.equals(rpcTypeEnum.name, name))
                .findFirst().orElse(null);
    }
}
