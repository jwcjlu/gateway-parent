package com.jwcjlu.gateway.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-26 10:54
 **/
@Data
public class BaseOption implements Serializable {
    private Long id;
    private String label;
    private Long value;

}
