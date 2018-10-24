package com.jwcjlu.gateway.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-05-11 17:34
 **/
@Data
public class ResponseResult<T> implements Serializable {

    private List<T> dataList;

    private Long totalCount;

}
