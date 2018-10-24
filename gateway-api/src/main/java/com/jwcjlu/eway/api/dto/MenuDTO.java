package com.jwcjlu.gateway.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * this is a class .
 * @author chengchuantuo
 *
 * @date 2018-06-21 11:05
 **/
@Data
public class MenuDTO implements Serializable {

    private Integer pageSize;

    private Integer pageNumber;

    private Long id;

    private String[] ids;

    private String name;

    private String url;

    private Long parentId;

}
