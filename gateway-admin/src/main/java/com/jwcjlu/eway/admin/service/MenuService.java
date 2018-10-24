
package com.jwcjlu.gateway.admin.service;

import com.jwcjlu.gateway.api.dto.MenuDTO;
import com.jwcjlu.gateway.api.dto.TreeDTO;
import com.jwcjlu.gateway.api.entity.Menu;
import com.jwcjlu.gateway.api.entity.RoleMenu;
import com.jwcjlu.gateway.api.vo.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * menu service .
 *
 * @author chengchuantuo
 * @date 2018-06-19 17:49
 **/

public interface MenuService {


    /**
     * get menu list.
     *
     * @return java.util.List
     */

    List<Menu> getMenuList();


    /**
     * get role menu list.
     *
     * @param id role id
     * @return java.util.Map
     */

    Map<String, Boolean> getMenuIdByRoleId(Integer id);


    /**
     * get menu.
     *
     * @return java.util.List
     */

    List<TreeDTO> getRoleCompetence();


    /**
     * delete role_menu by roleId.
     *
     * @param roleId role id
     * @return int
     */

    int deleteCompetenceByRoleId(Integer roleId);


    /**
     * save role_menu.
     *
     * @param roleMenu role_menu object
     * @return int
     */

    int saveCompetence(RoleMenu roleMenu);


    /**
     * get competence data by role id.
     *
     * @param roleId role id
     * @return java.util.List
     */

    List<RoleMenu> getCompetenceByRoleId(Integer roleId);


    /**
     * get role menu.
     *
     * @param roleId role id
     * @return java.util.Map
     */

    Map<String, Object> getRoleMenus(Integer roleId);


    /**
     * save menu.
     *
     * @param menu object
     * @return int
     */

    int saveMenu(Menu menu);


    /**
     * delete menu by id.
     *
     * @param id primary key
     * @return int
     */

    int deleteMenu(Long id);


    /**
     * get menu page list.
     *
     * @param dto package object to get param
     * @return java.util.List
     */

    ResponseResult<Menu> getMenuPageList(MenuDTO dto);


    /**
     * get menu by id.
     *
     * @param id primary key
     * @return
     */

    Menu getMenuById(Long id);


    /**
     * update menu by id.
     *
     * @param menu object
     * @return
     */

    int updateMenuById(Menu menu);

}

