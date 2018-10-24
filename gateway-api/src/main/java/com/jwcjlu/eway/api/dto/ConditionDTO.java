package com.jwcjlu.gateway.api.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-05-21 10:34
 **/
@Data
public class ConditionDTO implements Serializable {

    private String paramType;

    private String paramName;

    private String paramValue;

    private String operator;

    public String getParamName() {
        if (StringUtils.isNotEmpty(paramName)) {
            return paramName.trim();
        }
        return paramName;
    }

    public String getParamValue() {
        if (StringUtils.isNotEmpty(paramValue)) {
            return paramValue.trim();
        }
        return paramValue;
    }
}
