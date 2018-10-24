
package com.jwcjlu.gateway.admin.service.impl;

import com.jwcjlu.gateway.admin.service.EnumService;
import com.jwcjlu.gateway.common.enums.LoadBalanceEnum;
import com.jwcjlu.gateway.common.enums.MatchModeEnum;
import com.jwcjlu.gateway.common.enums.OperatorEnum;
import com.jwcjlu.gateway.common.enums.ParamTypeEnum;
import com.jwcjlu.gateway.common.enums.PluginEnum;
import com.jwcjlu.gateway.common.enums.RateLimiterTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * EnumServiceImpl.
 *
 * @author chengzhuantuo
 */

@Service("enumService")
public class EnumServiceImpl implements EnumService {
    @Override
    public List<String> getMatchModeList() {
        return Arrays.stream(MatchModeEnum.values())
                .map(MatchModeEnum:: getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getOperatorTypeList() {
        return Arrays.stream(OperatorEnum.values())
                .map(OperatorEnum:: getAlias).collect(Collectors.toList());
    }

    @Override
    public List<String> getParamTypeList() {
        return Arrays.stream(ParamTypeEnum.values())
                .map(ParamTypeEnum:: getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getPluginTypeList() {
        return Arrays.stream(PluginEnum.values())
                .map(PluginEnum:: getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getLoadBalanceTypeList() {
        return Arrays.stream(LoadBalanceEnum.values())
                .map(LoadBalanceEnum:: getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getRateLimiterTypeList() {
        return Arrays.stream(RateLimiterTypeEnum.values())
                .map(RateLimiterTypeEnum:: getName).collect(Collectors.toList());
    }
}

