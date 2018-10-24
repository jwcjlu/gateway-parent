
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.AppAuthMapper;
import com.jwcjlu.gateway.admin.service.AppAuthService;
import com.jwcjlu.gateway.admin.service.AppPluginService;
import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.admin.service.PluginService;
import com.jwcjlu.gateway.admin.service.RuleService;
import com.jwcjlu.gateway.admin.service.ServiceRuleService;
import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.dto.AppAuthDTO;
import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.entity.AppAuthExample;
import com.jwcjlu.gateway.api.entity.AppPlugin;
import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 */

@Service("appAuthService")
public class AppAuthServiceImpl implements AppAuthService {

    @Autowired(required = false)
    private AppAuthMapper appAuthMapper;

    @Autowired
    private AppPluginService appPluginService;

    @Autowired
    private EtcdService etcdService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ServiceRuleService serviceRuleService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private RuleService ruleService;

    @Override
    public int insertAppAuth(final AppAuth appAuth) {
        etcdService.loadAppAuthToEtcd(appAuth);
        return appAuthMapper.insertSelective(appAuth);
    }

    @Override
    public int updateAppAuth(final AppAuth appAuth) {
        etcdService.loadAppAuthToEtcd(appAuth);
        return appAuthMapper.updateByPrimaryKeySelective(appAuth);
    }

    @Override
    public ResponseResult<AppAuth> getList(final AppAuthDTO appAuthDTO) {
        AppAuthExample example = new AppAuthExample();
        if (StringUtils.isEmpty(appAuthDTO.getAppAuth())) {
            example.createCriteria().andUserIdEqualTo(appAuthDTO.getUserId());
        } else {
            example.createCriteria().andUserIdEqualTo(appAuthDTO.getUserId()).andAppKeyLike("%" + appAuthDTO.getAppAuth() + "%");
        }
        Integer pageNumber = appAuthDTO.getPageNumber();
        Integer pageSize = appAuthDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<AppAuth> list = appAuthMapper.selectByExample(example);
        list.forEach(appAuth -> {
            List<AppPlugin> appList = appPluginService.getListByAppId(appAuth.getId());
            List<Long> idList = new ArrayList<>();
            appList.forEach(app -> {
                idList.add(app.getPluginId());
            });
            appAuth.setIds(idList);
        });
        PageInfo page = new PageInfo(list);
        ResponseResult<AppAuth> result = new ResponseResult<>();
        result.setTotalCount(page.getTotal());
        result.setDataList(list);
        return result;
    }

    @Override
    public List<AppAuth> getList() {
        AppAuthExample example = new AppAuthExample();
        example.createCriteria();
        return appAuthMapper.selectByExample(example);
    }

    @Override
    public int deleteAppAuth(final Long id) {
        AppAuth appAuth = getAppAuthById(id);
        //update to etcd
        etcdService.deleteAppAuthFromEtcd(appAuth.getAppKey());
        List<Services> services = servicesService.getServiceByAppId(id);
        if (services.size() > 0) {
            services.forEach(service -> {
                servicesService.delete(service.getId());
            });
        }
        return appAuthMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AppAuth getAppAuthById(final Long id) {
        return appAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public AppAuth getAppAuthByAppKey(final String key, final Long userId) {
        AppAuthExample example = new AppAuthExample();
        example.createCriteria().andAppKeyEqualTo(key).andUserIdEqualTo(userId);
        List<AppAuth> appAuthList = appAuthMapper.selectByExample(example);
        if (appAuthList.size() > 0) {
            return appAuthList.get(0);
        }
        return new AppAuth();
    }
}
