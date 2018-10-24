package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.ServicesMapper;
import com.jwcjlu.gateway.admin.service.AppAuthService;
import com.jwcjlu.gateway.admin.service.ServiceRuleService;
import com.jwcjlu.gateway.admin.service.ServicesService;
import com.jwcjlu.gateway.api.dto.AppAuthDTO;
import com.jwcjlu.gateway.api.dto.ServiceDTO;
import com.jwcjlu.gateway.api.entity.AppAuth;
import com.jwcjlu.gateway.api.entity.AppServiceTree;
import com.jwcjlu.gateway.api.entity.BaseOption;
import com.jwcjlu.gateway.api.entity.Services;
import com.jwcjlu.gateway.api.entity.ServicesExample;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-07-25 14:33
 **/
@Service("servicesService")
public class ServicesServiceImpl implements ServicesService {

    @Autowired
    private ServicesMapper servicesMapper;

    @Autowired
    private AppAuthService appAuthService;

    @Autowired
    private ServiceRuleService serviceRuleService;

    @Override
    public int insert(Services service) {
        return servicesMapper.insertSelective(service);
    }

    @Override
    public int update(Services service) {
        return servicesMapper.updateByPrimaryKeySelective(service);
    }

    @Override
    public ResponseResult<Services> getPageList(ServiceDTO serviceDTO) {
        ServicesExample example = new ServicesExample();
        if (Objects.isNull(serviceDTO.getServiceKey())) {
            example.createCriteria().andUserIdEqualTo(serviceDTO.getUserId());
        } else {
            example.createCriteria().andUserIdEqualTo(serviceDTO.getUserId()).andServiceKeyLike("%" + serviceDTO.getServiceKey() + "%");
        }
        Integer pageNumber = serviceDTO.getPageNumber();
        Integer pageSize = serviceDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Services> list = servicesMapper.selectByExample(example);
        if (list.size() > 0) {
            list.forEach(service -> {
                AppAuth appAuth = appAuthService.getAppAuthById(service.getAppId());
                service.setAppKey(appAuth.getAppKey());
            });
        }

        PageInfo page = new PageInfo(list);
        ResponseResult<Services> result = new ResponseResult<>();
        result.setTotalCount(page.getTotal());
        result.setDataList(list);
        return result;
    }

    @Override
    public int delete(Long id) {
        serviceRuleService.deleteByRuleId(null, id, true);
        return servicesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Services getServiceById(Long id) {
        return servicesMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AppServiceTree> getPageListForRule(ServiceDTO serviceDTO) {
        Integer pageNumber = serviceDTO.getPageNumber();
        Integer pageSize = serviceDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        AppAuthDTO dto = new AppAuthDTO();
        dto.setPageNumber(pageNumber);
        dto.setPageSize(pageSize);
        dto.setUserId(serviceDTO.getUserId());
        ResponseResult<AppAuth> result = appAuthService.getList(dto);
        List<AppServiceTree> response = new ArrayList<>();
        if (result.getTotalCount() > 0) {
            result.getDataList().forEach(appAuth -> {
                AppServiceTree tree = new AppServiceTree();
                tree.setLabel(appAuth.getAppKey());
                List<BaseOption> optionList = new ArrayList<>();
                List<Services> services = getServiceByAppId(appAuth.getId());
                if (services.size() > 0) {
                    services.forEach(service -> {
                        BaseOption option = new BaseOption();
                        option.setId(service.getId());
                        option.setLabel(service.getServiceKey());
                        option.setValue(service.getId());
                        optionList.add(option);
                    });
                    tree.setOptions(optionList);
                }
                response.add(tree);
            });
        }
        return response;
    }

    @Override
    public List<Services> getServiceByAppId(Long appId) {
        ServicesExample example = new ServicesExample();
        example.createCriteria().andAppIdEqualTo(appId);
        return servicesMapper.selectByExample(example);
    }

    @Override
    public Boolean ifExists(String serviceKey) {
        ServicesExample example = new ServicesExample();
        example.createCriteria().andServiceKeyEqualTo(serviceKey);
        List<Services> list = servicesMapper.selectByExample(example);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }


}
