package com.jwcjlu.gateway.api.entity;

import com.jwcjlu.gateway.api.dto.etcd.PluginEtcdDTO;

import java.io.Serializable;
import java.util.Date;

public class Plugin implements Serializable {
    private Long id;

    private String pluginName;

    private Byte enabled;

    private Date timeCreated;

    private Date timeUpdated;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName == null ? null : pluginName.trim();
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

    public PluginEtcdDTO convert(Plugin plugin){
        PluginEtcdDTO dto = new PluginEtcdDTO();
        dto.setEnabled(plugin.getEnabled() == 1);
        dto.setId(plugin.getId());
        dto.setName(plugin.getPluginName());
        return dto;
    }
}