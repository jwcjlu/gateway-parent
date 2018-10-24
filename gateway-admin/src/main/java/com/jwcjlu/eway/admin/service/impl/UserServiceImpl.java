
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.DashboardUserMapper;
import com.jwcjlu.gateway.admin.service.UserService;
import com.jwcjlu.gateway.api.dto.UserDTO;
import com.jwcjlu.gateway.api.entity.DashboardUser;
import com.jwcjlu.gateway.api.entity.DashboardUserExample;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.utils.RawDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * user service.
 *
 * @author chengzhuantuo
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private DashboardUserMapper mapper;

    @Value("${userRawData}")
    private String userRawData;

    @Override
    public ResponseResult<DashboardUser> findByName(final UserDTO userDTO) {
        DashboardUserExample example = new DashboardUserExample();
        example.createCriteria().andUserNameLike("%" + userDTO.getUserName() + "%");
        example.setOrderByClause("id ASC");
        Integer pageNumber = userDTO.getPageNumber();
        Integer pageSize = userDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<DashboardUser> userList = mapper.selectByExample(example);
        ResponseResult<DashboardUser> result = new ResponseResult<>();
        result.setDataList(userList);
        PageInfo page = new PageInfo(userList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    @Override
    public int inserUser(final DashboardUser user) {
        return mapper.insertSelective(user);
    }

    @Override
    public int updateUser(final DashboardUser user) {
        return mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public ResponseResult<DashboardUser> getList(final UserDTO userDTO) {
        DashboardUserExample example = new DashboardUserExample();
        example.createCriteria();
        example.setOrderByClause("id ASC");
        Integer pageNumber = userDTO.getPageNumber();
        Integer pageSize = userDTO.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<DashboardUser> pluginList = mapper.selectByExample(example);
        ResponseResult<DashboardUser> result = new ResponseResult<>();
        result.setDataList(pluginList);
        PageInfo page = new PageInfo(pluginList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    @Override
    public int deleteUser(final Integer id) {
        return mapper.deleteByPrimaryKey(Long.valueOf(id));
    }

    @Override
    public DashboardUser login(final UserDTO userDTO) {
        DashboardUserExample example = new DashboardUserExample();
        example.createCriteria().andUserNameEqualTo(userDTO.getUserName()).andPasswordEqualTo(userDTO.getPassword());
        List<DashboardUser> userList = mapper.selectByExample(example);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return new DashboardUser();
    }

    @Override
    public DashboardUser getUserByName(final String name) {
        DashboardUserExample example = new DashboardUserExample();
        example.createCriteria().andUserNameEqualTo(name);
        List<DashboardUser> userList = mapper.selectByExample(example);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return new DashboardUser();
    }

    @Override
    public Boolean checkRawData(final String id) {
        return RawDataUtil.checkRawData(userRawData, id);
    }

}

