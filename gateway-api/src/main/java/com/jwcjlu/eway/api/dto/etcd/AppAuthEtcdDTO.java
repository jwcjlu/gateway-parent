package com.jwcjlu.gateway.api.dto.etcd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AppAuthZkDTO.
 * @author xiaoyu(Myth)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthEtcdDTO implements Serializable {

    private String appKey;

    private String appSecret;

    private Boolean enabled;
}
