package com.jwcjlu.gateway.admin.service.impl;

import com.jwcjlu.gateway.admin.mapper.ServiceRuleMapper;
import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.admin.service.RuleService;
import com.jwcjlu.gateway.admin.service.ServiceRuleService;
import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.api.entity.ServiceRule;
import com.jwcjlu.gateway.api.entity.ServiceRuleExample;
import com.jwcjlu.gateway.api.entity.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-26 13:52
 **/
@Service("serviceRuleService")
public class ServiceRuleServiceImpl implements ServiceRuleService {

    @Autowired
    private ServiceRuleMapper mapper;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private EtcdService etcdService;

    @Override
    public List<ServiceRule> getListByRuleId(Long ruleId, Long serviceId) {
        ServiceRuleExample example = new ServiceRuleExample();
        if (Objects.nonNull(ruleId)) {
            example.createCriteria().andRuleIdEqualTo(ruleId);
        }
        if (Objects.nonNull(serviceId)) {
            example.createCriteria().andServiceIdEqualTo(serviceId);
        }
        return mapper.selectByExample(example);
    }

    @Override
    public int insert(ServiceRule serviceRule) {
        return mapper.insertSelective(serviceRule);
    }

    @Override
    public void deleteByRuleId(Long ruleId, Long serviceId, Boolean flag) {

        ServiceRuleExample example = new ServiceRuleExample();
        if(Objects.nonNull(ruleId) && Objects.nonNull(serviceId)){
            example.createCriteria().andServiceIdEqualTo(serviceId).andRuleIdEqualTo(ruleId);
        }else{
            if (Objects.nonNull(ruleId)) {
                if (flag) {
                    List<ServiceRule> list = getListByRuleId(ruleId, null);
                    if (list.size() > 0) {
                        list.forEach(rService -> {
                            Services service = servicesService.getServiceById(rService.getServiceId());
                            Rule rule = ruleService.getRule(ruleId);
                            Plugin plugin = pluginService.getPluginById(rule.getPluginId());
                            etcdService.deleteRule(plugin.getPluginName(), service.getServiceKey());
                            etcdService.deleteServicePlugins(plugin.getPluginName(), service.getServiceKey());
                        });
                    }
                }
                example.createCriteria().andRuleIdEqualTo(ruleId);
            }
            if (Objects.nonNull(serviceId)) {
                if (flag) {
                    //先删除etcd上节点信息
                    List<ServiceRule> ruleServiceList = getListByRuleId(null, serviceId);
                    ruleServiceList.stream().forEach(ruleServices -> {
                        Services services1 = servicesService.getServiceById(ruleServices.getServiceId());
                        Plugin plugin = pluginService.getPluginById(ruleService.getRule(ruleServices.getRuleId()).getPluginId());
                        etcdService.deleteRule(plugin.getPluginName(), services1.getServiceKey());
                        etcdService.deleteServicePlugins(plugin.getPluginName(), services1.getServiceKey());
                    });
                }
                example.createCriteria().andServiceIdEqualTo(serviceId);
            }
        }
        mapper.deleteByExample(example);
    }
}
