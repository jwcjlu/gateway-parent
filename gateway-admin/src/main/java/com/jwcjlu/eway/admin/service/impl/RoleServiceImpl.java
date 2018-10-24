
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.RoleMapper;
import com.jwcjlu.gateway.admin.service.RoleService;
import com.jwcjlu.gateway.api.dto.RoleDTO;
import com.jwcjlu.gateway.api.entity.Role;
import com.jwcjlu.gateway.api.entity.RoleExample;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.utils.RawDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * role service.
 *
 * @author chengchuantuo
 * @date 2018-06-09 09:38
 **/

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Value("${roleDataId}")
    private String roleDataId;

    @Override
    public List<Role> getList() {
        RoleExample example = new RoleExample();
        example.createCriteria();
        return roleMapper.selectByExample(example);
    }

    @Override
    public int saveRole(final Role role) {
        return roleMapper.insertSelective(role);
    }

    @Override
    public int updateRole(final Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int deleteRole(final Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ResponseResult<Role> getPageList(final RoleDTO dto) {
        RoleExample example = new RoleExample();
        example.createCriteria();
        Integer pageNumber = dto.getPageNumber();
        Integer pageSize = dto.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Role> roleList = roleMapper.selectByExample(example);
        ResponseResult<Role> result = new ResponseResult<>();
        result.setDataList(roleList);
        PageInfo page = new PageInfo(roleList);
        result.setTotalCount(page.getTotal());
        return result;
    }

    @Override
    public Boolean checkExist(final String role) {
        Boolean flag = false;
        RoleExample example = new RoleExample();
        example.createCriteria().andRoleEqualTo(role);
        List<Role> list = roleMapper.selectByExample(example);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Role getRoleById(final Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean checkRawData(final String id) {
        return RawDataUtil.checkRawData(roleDataId, id);
    }
}

