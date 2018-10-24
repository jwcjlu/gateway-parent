package com.jwcjlu.gateway.api.dto.etcd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-30 15:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginEtcdDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;
}
