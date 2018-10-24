
package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.RoleDTO;
import com.jwcjlu.gateway.api.entity.Role;
import com.jwcjlu.gateway.api.vo.ResponseResult;

import java.util.List;


/**
 * this is a class .
 *
 * @author chengchuantuo
 * @date 2018-06-09 09:36
 **/

public interface RoleService {


    /**
     * get rule list.
     *
     * @return java.util.List
     */

    List<Role> getList();


    /**
     * save role.
     *
     * @param role entity
     * @return int
     */

    int saveRole(Role role);


    /**
     * update role.
     *
     * @param role entity
     * @return int
     */

    int updateRole(Role role);


    /**
     * delete role.
     *
     * @param id primary key
     * @return int
     */

    int deleteRole(Integer id);


    /**
     * get page list.
     *
     * @param dto package entity
     * @return com.jwcjlu.skyway.api.vo.ResponseResult
     */

    ResponseResult<Role> getPageList(RoleDTO dto);


    /**
     * check if has been existed.
     *
     * @param role role name
     * @return java.lang.Boolean
     */

    Boolean checkExist(String role);


    /**
     * get role by id.
     *
     * @param id primary key
     * @return com.jwcjlu.skyway.api.entity.Role
     */

    Role getRoleById(Integer id);


    /**
     * if it is raw data.
     *
     * @param id primary key
     * @return java.lang.Boolean
     */

    Boolean checkRawData(String id);
}

