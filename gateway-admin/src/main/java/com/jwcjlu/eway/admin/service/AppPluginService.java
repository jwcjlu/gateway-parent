package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.entity.AppPlugin;

import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-25 13:47
 **/
public interface AppPluginService {

   int deleteByAppAuth(Long id);

   int insertAppPlugin(AppPlugin appPlugin);

   List<AppPlugin> getListByAppId(Long appId);



}
