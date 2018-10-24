package com.jwcjlu.gateway.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-06-19 14:08
 **/
@Data
public class TreeDTO implements Serializable{

    private Long id;

    private String label;

    private Boolean disable;

    private List<TreeDTO> children;

}
