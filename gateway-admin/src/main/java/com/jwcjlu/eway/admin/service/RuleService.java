
package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.RuleDTO;
import com.jwcjlu.gateway.api.entity.Rule;
import com.jwcjlu.gateway.api.vo.ResponseResult;


/**
 * RuleService.
 *
 * @author chengchuantuo
 */


public interface RuleService {

    /**
     * add rule.
     *
     * @param rule object
     * @param dto  object
     * @return int
     */

    int insertRule(Rule rule, RuleDTO dto);


    /**
     * update rule.
     *
     * @param dto  object
     * @param rule object
     */

    void updateRule(Rule rule, RuleDTO dto);


    /**
     * delete rule.
     *
     * @param id primary key
     * @return int
     */

    int deleteRule(Long id);


    /**
     * get rule by primary key.
     *
     * @param id primary key
     * @return int
     */

    Rule getRule(Long id);


    /**
     * get rule list.
     *
     * @param dto package to get param
     * @return List
     */

    ResponseResult<Rule> getList(RuleDTO dto);

}

