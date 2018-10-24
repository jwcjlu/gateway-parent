package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.ServiceDTO;
import com.jwcjlu.gateway.api.entity.AppServiceTree;
import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.vo.ResponseResult;

import java.util.List;

public interface ServicesService {

    int insert(Services service);

    int update(Services service);

    ResponseResult<Services> getPageList(ServiceDTO serviceDTO);

    int delete(Long id);

    Services getServiceById(Long id);

    List<AppServiceTree> getPageListForRule(ServiceDTO serviceDTO);

    List<Services> getServiceByAppId(Long appId);

    Boolean ifExists(String serviceKey);
}
