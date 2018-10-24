package com.jwcjlu.gateway.api.entity;

import com.jwcjlu.gateway.api.dto.etcd.AppAuthEtcdDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AppAuth implements Serializable {
    private Long id;

    private String appKey;

    private String appSecret;

    private Long userId;

    private Byte enabled;

    private List<Long> ids;

    private Date timeCreated;

    private Date timeUpdated;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public AppAuthEtcdDTO convert(AppAuth appAuth) {
        AppAuthEtcdDTO dto = new AppAuthEtcdDTO();
        dto.setAppKey(appAuth.getAppKey());
        dto.setAppSecret(appAuth.appSecret);
        dto.setEnabled(appAuth.enabled == 1);
        return dto;
    }
}