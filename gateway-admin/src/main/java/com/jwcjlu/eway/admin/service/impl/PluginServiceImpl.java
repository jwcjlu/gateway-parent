
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.PluginMapper;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.api.dto.ApiDTO;
import com.jwcjlu.gateway.api.entity.Plugin;
import com.jwcjlu.gateway.api.entity.PluginExample;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.utils.RawDataUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * PluginServiceImpl.
 *
 * @author chengzhuantuo
 */

@Service("pluginService")
public class PluginServiceImpl implements PluginService {

    @Autowired
    private PluginMapper pluginMapper;

    @Value("${pluginRawData}")
    private String pluginRawData;

    @Override
    public int updatePlugin(final Plugin plugin) {
        return pluginMapper.updateByPrimaryKeySelective(plugin);
    }

    @Override
    public ResponseResult<Plugin> getList(final ApiDTO apiDTO) {
        PluginExample example = new PluginExample();
        if (StringUtils.isNotEmpty(apiDTO.getName())) {
            example.createCriteria().andPluginNameLike("%" + apiDTO.getName() + "%");
        } else {
            example.createCriteria();
        }

        example.setOrderByClause("id ASC");
        Integer pageNumber = apiDTO.getPageNumber();
        Integer pageSize = apiDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Plugin> pluginList = pluginMapper.selectByExample(example);
        ResponseResult<Plugin> result = new ResponseResult<>();
        result.setDataList(pluginList);
        PageInfo page = new PageInfo(pluginList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    @Override
    public Plugin getPlugin(final String pluginName) {
        PluginExample example = new PluginExample();
        example.createCriteria().andPluginNameEqualTo(pluginName);
        List<Plugin> list = pluginMapper.selectByExample(example);
        if (list.size() > 0) {
            return list.get(0);
        }
        return new Plugin();
    }

    @Override
    public Plugin getPluginById(Long pluginId) {
        return pluginMapper.selectByPrimaryKey(pluginId);
    }

    @Override
    public Boolean checkRawData(final String id) {
        return RawDataUtil.checkRawData(pluginRawData, id);
    }
}
