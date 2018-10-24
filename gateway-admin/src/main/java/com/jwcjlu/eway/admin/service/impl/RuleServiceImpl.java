
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.RuleMapper;
import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.admin.service.RuleService;
import com.jwcjlu.gateway.admin.service.ServiceRuleService;
import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.dto.RuleDTO;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.api.entity.RuleExample;
import com.jwcjlu.gateway.api.entity.ServiceRule;
import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


/**
 * RuleServiceImpl.
 *
 * @author chengzhuantuo
 */

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

    private static Long NUM = 0L;

    @Autowired
    private RuleMapper ruleMapper;

    @Autowired
    private ServiceRuleService serviceRuleService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private EtcdService etcdService;

    @Override
    public int insertRule(Rule rule, RuleDTO dto) {
        ruleMapper.insertSelective(rule);
        doOperate(rule, dto);
        return 0;
    }

    @Override
    public void updateRule(Rule rule, RuleDTO dto) {
        doOperate(rule, dto);
        ruleMapper.updateByPrimaryKeySelective(rule);
    }

    @Override
    public int deleteRule(Long id) {
        serviceRuleService.deleteByRuleId(id, null, true);
        return ruleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Rule getRule(Long id) {
        return ruleMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseResult<Rule> getList(final RuleDTO apiDTO) {
        Plugin plugin = pluginService.getPlugin(apiDTO.getPlugin());
        RuleExample example = new RuleExample();
        example.createCriteria().andUserIdEqualTo(apiDTO.getUserId()).andPluginIdEqualTo(plugin.getId());
        example.setOrderByClause("id ASC");
        Integer pageNumber = apiDTO.getPageNumber();
        Integer pageSize = apiDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Rule> ruleList = ruleMapper.selectByExample(example);
        if (ruleList.size() > 0) {
            ruleList.forEach(rule -> {
                List<String> serviceList = new ArrayList<>();
                List<Long> serviceIdList = new ArrayList<>();
                List<ServiceRule> ruleServiceObjList = serviceRuleService.getListByRuleId(rule.getId(), null);
                ruleServiceObjList.forEach(obj -> {
                    Services serviceObj = servicesService.getServiceById(obj.getServiceId());
                    serviceList.add(Objects.nonNull(serviceObj) ? serviceObj.getServiceKey() : "");
                    serviceIdList.add(Objects.nonNull(serviceObj) ? serviceObj.getId() : null);
                });
                rule.setIds(serviceIdList);
                rule.setServiceList(serviceList.toString().replace("[", "").replace("]", ""));
            });
        }
        ResponseResult<Rule> result = new ResponseResult<>();
        result.setDataList(ruleList);
        PageInfo page = new PageInfo(ruleList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    public void doOperate(Rule rule, RuleDTO dto) {
        if (dto.getRemoveIds().length > 0) {
            Arrays.stream(dto.getRemoveIds()).forEach(removeId -> {
                if (!Objects.deepEquals(removeId, NUM)) {
                    Services service = servicesService.getServiceById(removeId);
                    etcdService.deleteRule(dto.getPlugin(), service.getServiceKey());
                    serviceRuleService.deleteByRuleId(rule.getId(),service.getId(),false);
                    etcdService.deleteServicePlugins(dto.getPlugin(),service.getServiceKey());
                }
            });
        }

       // serviceRuleService.deleteByRuleId(rule.getId(), null, false);
        Calendar calendar = Calendar.getInstance();
        if (dto.getIds().length > 0) {
            Long[] ids = dto.getIds();
            for (int i = 0; i < ids.length; i++) {
                if (!Objects.deepEquals(ids[i], NUM)) {
                    ServiceRule ruleServiceObj = new ServiceRule();
                    ruleServiceObj.setRuleId(rule.getId());
                    ruleServiceObj.setServiceId(ids[i]);
                    ruleServiceObj.setTimeCreated(calendar.getTime());
                    ruleServiceObj.setTimeUpdated(calendar.getTime());
                    serviceRuleService.insert(ruleServiceObj);
                    //
                    Services service = servicesService.getServiceById(ids[i]);
                    etcdService.loadRule(dto.getPlugin(), service.getServiceKey(), rule);
                    etcdService.loadServicePlugins(dto.getPlugin(), service.getServiceKey(),rule.getId());
                }
            }
        }
        if(dto.getEqualNums().length>0){
            for(int h = 0 ; h<dto.getEqualNums().length;h++){
                Services service = servicesService.getServiceById(dto.getEqualNums()[h]);
                etcdService.loadRule(dto.getPlugin(), service.getServiceKey(), rule);
                etcdService.loadServicePlugins(dto.getPlugin(), service.getServiceKey(),rule.getId());
            }
        }
    }
}
