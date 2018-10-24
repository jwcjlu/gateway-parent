package com.jwcjlu.gateway.admin.service.impl;

import com.jwcjlu.gateway.admin.mapper.AppPluginMapper;
import com.jwcjlu.gateway.admin.service.AppPluginService;
import com.jwcjlu.gateway.api.entity.AppPlugin;
import com.jwcjlu.gateway.api.entity.AppPluginExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-25 13:49
 **/
@Service("appPluginService")
public class AppPluginServiceImpl implements AppPluginService {

    @Autowired
    private AppPluginMapper mapper;

    @Override
    public int deleteByAppAuth(Long id) {
        AppPluginExample example = new AppPluginExample();
        example.createCriteria().andAppIdEqualTo(id);
        return mapper.deleteByExample(example);
    }

    @Override
    public int insertAppPlugin(AppPlugin appPlugin) {
        return mapper.insertSelective(appPlugin);
    }

    @Override
    public List<AppPlugin> getListByAppId(Long appId) {
        AppPluginExample example = new AppPluginExample();
        example.createCriteria().andAppIdEqualTo(appId);
        return mapper.selectByExample(example);
    }
}
