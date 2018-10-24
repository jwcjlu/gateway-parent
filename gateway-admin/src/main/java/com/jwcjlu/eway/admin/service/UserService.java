
package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.UserDTO;
import com.jwcjlu.gateway.api.entity.DashboardUser;
import com.jwcjlu.gateway.api.vo.ResponseResult;


/**
 * UserService.
 *
 * @author chengchuantuo
 */


public interface UserService {

    /**
     * according to userName to get user.
     *
     * @param userDTO userDTO
     * @return com.jwcjlu.skyway.api.entity.DashBoardUser
     */

    ResponseResult<DashboardUser> findByName(UserDTO userDTO);


    /**
     * insert user.
     *
     * @param user user
     * @return int
     */

    int inserUser(DashboardUser user);


    /**
     * update user.
     *
     * @param user user
     * @return int
     */

    int updateUser(DashboardUser user);


    /**
     * get user list.
     *
     * @param userDTO userDTO
     * @return java.util.List{<com.jwcjlu.skyway.api.entity.DashBoardUser/>}
     */

    ResponseResult<DashboardUser> getList(UserDTO userDTO);


    /**
     * delete user.
     *
     * @param id primary key
     * @return int
     */

    int deleteUser(Integer id);


    /**
     * login.
     *
     * @param userDTO entity
     * @return com.jwcjlu.skyway.api.entity.DashBoardUser
     */

    DashboardUser login(UserDTO userDTO);


    /**
     * get user by user name.
     *
     * @param name userName
     * @return com.jwcjlu.skyway.api.entity.DashBoardUser
     */

    DashboardUser getUserByName(String name);


    /**
     * if it is raw data.
     *
     * @param id primary key
     * @return java.lang.Boolean
     */

    Boolean checkRawData(String id);
}

