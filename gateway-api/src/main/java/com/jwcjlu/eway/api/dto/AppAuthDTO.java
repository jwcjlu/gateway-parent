package com.jwcjlu.gateway.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-05-16 13:43
 **/
@Data
public class AppAuthDTO implements Serializable {
    private String appAuth;
    private String enabled;
    private Long userId;
    private Long id;
    private Integer pageSize;
    private Integer pageNumber;
    private String[] ids;
    private String[] removeIds;
}
