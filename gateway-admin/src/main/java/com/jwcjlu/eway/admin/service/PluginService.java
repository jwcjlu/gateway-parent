package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.ApiDTO;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.vo.ResponseResult;


public interface PluginService {

    /**
     * update plugin.
     *
     * @param plugin
     * @return int
     */
    int updatePlugin(Plugin plugin);

     /**
     * get plugin list.
     *
     * @param apiDTO apiDTO
     * @return java.util.List com.jwcjlu.skyway.api.entity.Plugin
     */
    ResponseResult<Plugin> getList(ApiDTO apiDTO);

    /**
     * get plugin by plugin name.
     *
     * @param pluginName plugin name
     * @return Plugin
     */
    Plugin getPlugin(String pluginName);

    /**
     * get plugin by id.
     *
     * @param pluginId plugin id
     * @return Plugin
     */
    Plugin getPluginById(Long pluginId);

    /**
     * this is a function.
     *
     * @param id
     * @return java.lang.Boolean
     */
    Boolean checkRawData(String id);
}
