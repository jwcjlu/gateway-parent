/*
package com.jwcjlu.gateway.admin.service;

import ApiDTO;
import com.jwcjlu.gateway.api.entity.Selector;
import ResponseResult;

import java.util.List;

*/
/**
 * SelectorService.
 *
 * @author chengchuantuo
 *//*


public interface SelectorService {

    */
/**
     * according pluginId to get selector List.
     *
     * @param apiDTO apiDTO
     * @return java.util.List <com.jwcjlu.skyway.api.entity.Selector/>
     **//*

    ResponseResult<Selector> findBySid(ApiDTO apiDTO);

    */
/**
     * insert selector.
     *
     * @param selector object
     * @return int
     *//*

    int insertSelector(Selector selector);

    */
/**
     * update selector condition.
     *
     * @param selector object
     * @return int
     *//*

    int updateSelector(Selector selector);

    */
/**
     * delete selector .
     *
     * @param id selectorId
     * @return int
     *//*

    int deleteSelector(String id);

    */
/**
     * get selector by primary key.
     *
     * @param id selectorId
     * @return int
     *//*

    Selector getSelector(String id);

    */
/**
     * get all selector by pluginName.
     *
     * @param pluginName pluginName
     * @return java.util.List
     *//*

    List<Selector> getList(String pluginName);

    */
/**
     * get selector by name.
     *
     * @param name     selector name
     * @param pluginId plugin id
     * @param id       selector id
     * @return com.jwcjlu.skyway.api.entity.Selector
     *//*

    Selector getSelectorByName(String name, Long pluginId, String id);

    */
/**
     * this is a function.
     *
     * @param type     0:全流量选择器  1：自定义选择器
     * @param pluginId plugin id
     * @param id       selector id
     * @return com.jwcjlu.skyway.api.entity.Selector
     *//*

    Selector getSelectorByType(String type, Long pluginId, String id);
}
*/
