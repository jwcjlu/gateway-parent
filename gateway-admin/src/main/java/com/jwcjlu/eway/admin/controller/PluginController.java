package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.api.dto.ApiDTO;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.result.AjaxResult;
import com.jwcjlu.gateway.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Objects;

/**
 * controller.
 *
 * @author chengchuantuo
 */

@RestController
@RequestMapping("/gateway")
public class PluginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);

    @Autowired
    private PluginService pluginService;

    @Autowired
    private EtcdService etcdService;

    /**
     * get plugin page list.
     *
     * @param apiDTO
     * @return AjaxResult
     */
    @PostMapping("/plugin/pluginList")
    public AjaxResult pluginList(@RequestBody final ApiDTO apiDTO) {
        ResponseResult<Plugin> result;
        result = pluginService.getList(apiDTO);
        return AjaxResult.success(result);
    }

    /**
     * update plugin.
     *
     * @param dto package object to get param
     * @return AjaxResult
     */
    @PostMapping("/plugin/update")
    public AjaxResult update(@RequestBody final ApiDTO dto) {
        LogUtils.debug(LOGGER, () -> dto.toString() + "plugin save or update");
        Calendar calendar = Calendar.getInstance();
        Plugin plugin = new Plugin();
        plugin.setId(Objects.isNull(dto.getId()) ? null : dto.getId());
        plugin.setTimeUpdated(calendar.getTime());
        plugin.setPluginName(dto.getName());
        plugin.setEnabled(Byte.valueOf(dto.getEnable()));
        pluginService.updatePlugin(plugin);
        //load plugin to etcd
        etcdService.loadPlugin(plugin);
        return AjaxResult.success(dto);
    }


}
