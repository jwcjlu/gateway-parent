package com.jwcjlu.gateway.api.dto;

import com.jwcjlu.gateway.api.convert.ConcurrencyHandle;
import com.jwcjlu.gateway.api.convert.DivideUpstream;
import lombok.Data;

import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-26 11:05
 **/
@Data
public class RuleDTO {

    private Long id;
    private String ruleName;
    private String plugin;
    private Long userId;
    private Integer pageSize;
    private Integer pageNumber;
    private ConcurrencyHandle concurrencyHandle;
    private List<DivideUpstream> domains;
    private Long[] removeIds;

    private String loadBalance;

    private int weightType;

    private Long[] ids;
    private Long[] equalNums;
}
