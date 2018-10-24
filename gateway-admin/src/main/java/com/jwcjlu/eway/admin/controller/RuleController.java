
package com.jwcjlu.gateway.admin.controller;

import com.alibaba.fastjson.JSON;
import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.admin.service.RuleService;
import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.convert.DivideHandle;
import com.jwcjlu.gateway.api.dto.ApiDTO;
import com.jwcjlu.gateway.api.dto.RuleDTO;
import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.enums.PluginEnum;
import com.jwcjlu.gateway.common.result.AjaxResult;
import com.jwcjlu.gateway.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;


/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 */

@RestController
@RequestMapping("/gateway/rule")
public class RuleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleController.class);

    private static Long NUM = 0L;

    @Autowired(required = false)
    private RuleService ruleService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private EtcdService etcdService;

    /**
     * get rule list by selector.
     *
     * @param ruleDTO package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/ruleList")
    public AjaxResult ruleList(@RequestBody final RuleDTO ruleDTO) {
        if (StringUtils.isEmpty(ruleDTO.getPlugin())) {
            return AjaxResult.error("获取插件为空");
        }
        ResponseResult<Rule> ruleResult;
        ruleResult = ruleService.getList(ruleDTO);
        return AjaxResult.success(ruleResult);
    }

    /**
     * delete rule.
     *
     * @param apiDTO package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/removeRule")
    public AjaxResult removeRule(@RequestBody final ApiDTO apiDTO) {
        LogUtils.debug(LOGGER, () -> apiDTO.getId() + "规则删除");
        if (apiDTO.getIds().length > 0) {
            Arrays.stream(apiDTO.getIds()).forEach(id -> {
                ruleService.deleteRule(id);
            });
        }
        return AjaxResult.success(true);
    }

    /**
     * save rule.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/saveRule")
    public AjaxResult saveRule(@RequestBody final RuleDTO dto) {
        LogUtils.debug(LOGGER, () -> dto.toString() + "save rule");
        if (dto.getIds().length > 0) {
            Long[] ids = dto.getIds();
            for (int i = 0; i < ids.length; i++) {
                if (!Objects.deepEquals(ids[i], NUM)) {
                    Services service = servicesService.getServiceById(ids[i]);
                    if (etcdService.ifExistedServicePlugins(dto.getPlugin(), service.getServiceKey(), dto.getId())) {
                        return AjaxResult.error("该规则所选的服务列表在该插件中已存在，请重新选择");
                    }
                }
            }
        }

        Rule rule = new Rule();
        Calendar calendar = Calendar.getInstance();
        rule.setId(dto.getId());
        rule.setTimeUpdated(calendar.getTime());
        rule.setPluginId(pluginService.getPlugin(dto.getPlugin()).getId());
        rule.setRuleName(dto.getRuleName());
        rule.setUserId(dto.getUserId());

        if (PluginEnum.CONCURRENCY.getName().equals(dto.getPlugin())) {
            rule.setHandler(JSON.toJSONString(dto.getConcurrencyHandle()));
        } else if (PluginEnum.DIVIDE.getName().equals(dto.getPlugin())) {
            DivideHandle handle = new DivideHandle();
            handle.setLoadBalance(dto.getLoadBalance());
            handle.setWeightType(dto.getWeightType());
            handle.setUpstreamList(dto.getDomains());
            rule.setHandler(JSON.toJSONString(handle));
        }

        if (Objects.nonNull(dto.getId())) {
            ruleService.updateRule(rule, dto);
        } else {
            rule.setTimeCreated(calendar.getTime());
            ruleService.insertRule(rule, dto);
        }
        return AjaxResult.success(true);
    }

}
