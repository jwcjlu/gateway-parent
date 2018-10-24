package com.jwcjlu.gateway.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-25 14:29
 **/
@Data
public class ServiceDTO implements Serializable {

    private Long userId;

    private Long id;

    private String[] ids;

    private Long appId;

    private String desc;

    private String serviceKey;

    private Integer pageSize;

    private Integer pageNumber;

    @Override
    public String toString() {
        return "ServiceDTO{" +
                "userId=" + userId +
                ", id=" + id +
                ", appAuthId=" + appId +
                ", desc='" + desc + '\'' +
                ", serviceKey='" + serviceKey + '\'' +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                '}';
    }
}
