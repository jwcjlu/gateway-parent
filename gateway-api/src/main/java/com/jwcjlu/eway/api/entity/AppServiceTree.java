package com.jwcjlu.gateway.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-26 10:52
 **/
@Data
public class AppServiceTree implements Serializable {
    private String label;
    private List<BaseOption> options;
}
