package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.dto.ServiceDTO;
import com.jwcjlu.gateway.api.entity.AppServiceTree;
import com.jwcjlu.gateway.api.entity.Services;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-25 14:41
 **/
@RestController
@RequestMapping("/gateway")
public class ServicesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicesController.class);

    @Autowired
    private ServicesService service;

    @PostMapping("/service/getList")
    public AjaxResult getServiceList(@RequestBody final ServiceDTO serviceDTO) {
        LogUtils.debug(LOGGER, () -> serviceDTO.toString());
        ResponseResult<Services> responseResult = service.getPageList(serviceDTO);
        return AjaxResult.success(responseResult);
    }

    @PostMapping("/service/updateOrInsert")
    public AjaxResult update(@RequestBody final ServiceDTO serviceDTO) {
        LogUtils.debug(LOGGER, () -> serviceDTO.toString());
        Services serviceObj = new Services();
        serviceObj.setAppId(serviceDTO.getAppId());
        serviceObj.setDescription(serviceDTO.getDesc());
        serviceObj.setUserId(serviceDTO.getUserId());
        if (service.ifExists(serviceDTO.getServiceKey())) {
            return AjaxResult.error("serviceKey重复,请重新输入");
        }
        serviceObj.setServiceKey(serviceDTO.getServiceKey());
        Calendar calendar = Calendar.getInstance();
        serviceObj.setTimeUpdated(calendar.getTime());
        if (Objects.isNull(serviceDTO.getId())) {
            serviceObj.setTimeCreated(calendar.getTime());
            service.insert(serviceObj);
        } else {
            serviceObj.setId(serviceDTO.getId());
            service.update(serviceObj);
        }
        return AjaxResult.success(serviceDTO);
    }

    @PostMapping("/service/delete")
    public AjaxResult delete(@RequestBody final ServiceDTO serviceDTO) {
        LogUtils.debug(LOGGER, () -> serviceDTO.toString());
        Arrays.stream(serviceDTO.getIds()).forEach(id -> service.delete(Long.parseLong(id)));
        return AjaxResult.success(serviceDTO);
    }


    @PostMapping("/service/getServiceListForRule")
    public AjaxResult getServiceListForRule(@RequestBody final ServiceDTO serviceDTO) {
        LogUtils.debug(LOGGER, () -> serviceDTO.toString());
        List<AppServiceTree> responseResult = service.getPageListForRule(serviceDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("tree", responseResult);
        return AjaxResult.success(map);
    }
}
