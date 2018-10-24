package com.jwcjlu.gateway.api.dto;

import lombok.Data;

/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 */
@Data
public class ApiDTO {

    private Integer pageSize;

    private Integer pageNumber;

    private Long[] ids;

    private String plugin;

    private Long id;

    private String name;

    private String enable;

    private String selectorId;

}
