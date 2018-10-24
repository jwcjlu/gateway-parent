
package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.EnumService;
import com.jwcjlu.gateway.common.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>Description: .</p>
 * get enumList controller
 *
 * @author chengchuantuo
 */

@RestController
@RequestMapping("/gateway")
public class EnumController {

    @Autowired
    private EnumService enumService;


    /**
     * require metchMode  list.
     *
     * @return AjaxResult
     */

    @PostMapping("/enum/metchModeEnum")
    public AjaxResult getMatchModeList() {

        List<String> list = enumService.getMatchModeList();
        return AjaxResult.success(list);
    }


    /**
     * require operator type interface.
     *
     * @return AjaxResult
     */

    @PostMapping("/enum/OperatorTypeEnum")
    public AjaxResult getOperatorTypeList() {
        List<String> list = enumService.getOperatorTypeList();
        return AjaxResult.success(list);
    }


    /**
     * require param type interface.
     *
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/enum/ParamTypeEnum")
    public AjaxResult getParamTypeList() {
        List<String> list = enumService.getParamTypeList();
        return AjaxResult.success(list);
    }

    /**
     * require param type interface.
     *
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/enum/rateLimiterTypeEnum")
    public AjaxResult getrateLimiterTypeList() {

        return AjaxResult.success(null);
    }


    /**
     * get enum list.
     *
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/enum/getEnum")
    public AjaxResult getPluginTypeList() {

       /* List<String> oList = enumService.getOperatorTypeList();
        List<EnumDTO> operatorTypeList = new ArrayList<>();*/
      /*  oList.forEach(x -> {
            EnumDTO dto = new EnumDTO();
            dto.setDisabled(false);
            dto.setName(x);
            operatorTypeList.add(dto);
        });
        List<String> pluginTypeList = enumService.getPluginTypeList();
        List<String> paramTypeList = enumService.getParamTypeList();
        List<String> matchModeList = enumService.getMatchModeList();
        List<String> loadBalanceTypeList = enumService.getLoadBalanceTypeList();*/
        Map<String, Object> map = new HashMap<>(6);
        List<String> rateLimiterTypeList = enumService.getRateLimiterTypeList();
        map.put("rateLimiterTypeList", rateLimiterTypeList);
       /* map.put("pluginTypeList", pluginTypeList);
        map.put("paramTypeList", paramTypeList);
        map.put("matchModeList", matchModeList);
        map.put("operatorTypeList", operatorTypeList);
        map.put("loadBalanceTypeList", loadBalanceTypeList);*/
        return AjaxResult.success(map);
    }
}

