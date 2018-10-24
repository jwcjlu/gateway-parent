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
public enum HttpMethodEnum {

    /**
     * Get http method enum.
     */
    GET("get"),

    /**
     * Post http method enum.
     */
    POST("post"),

    /**
     * Put http method enum.
     */
    PUT("put"),

    /**
     * Delete http method enum.
     */
    DELETE("delete");

    private final String name;

    /**
     * 转化封装.
     *
     * @param name 名称
     * @return HttpMethodEnum
     */
    public static HttpMethodEnum buildByName(final String name) {
        return Arrays.stream(HttpMethodEnum.values())
                .filter(httpMethodEnum -> Objects.equals(httpMethodEnum.name, name))
                .findFirst().orElse(null);

    }

}
