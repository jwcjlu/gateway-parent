package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.entity.ServiceRule;

import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-26 13:51
 **/
public interface ServiceRuleService {

    List<ServiceRule> getListByRuleId(Long ruleId, Long serviceId);

    int insert(ServiceRule serviceRule);

    void deleteByRuleId(Long ruleId, Long serviceId, Boolean flag);
}
