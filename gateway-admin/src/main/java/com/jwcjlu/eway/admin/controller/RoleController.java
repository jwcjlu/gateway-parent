
package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.MenuService;
import com.jwcjlu.gateway.admin.service.RoleService;
import com.jwcjlu.gateway.api.dto.RoleDTO;
import com.jwcjlu.gateway.api.entity.Role;
import com.jwcjlu.gateway.api.entity.RoleMenu;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.result.AjaxResult;
import com.jwcjlu.gateway.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * this is a class .
 *
 * @author chengchuantuo
 * @date 2018-06-09 10:52
 **/

@RestController
@RequestMapping("/gateway")
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;


    /**
     * get rule list by selector.
     *
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/role/roleList")
    public AjaxResult roleList() {
        List<Role> list = roleService.getList();
        Map<String, Object> map = new HashMap<>();
        if (Objects.nonNull(list)) {
            list.forEach(role -> {
                List<Long> idList = new ArrayList<>();
                List<RoleMenu> roleMenuList = menuService.getCompetenceByRoleId(role.getId());
                if (Objects.nonNull(roleMenuList)) {
                    roleMenuList.forEach(roleMenu -> idList.add(roleMenu.getMenuId()));
                }
                role.setCompetence(idList.toString());
            });
        }
        map.put("dataList", list);
        return AjaxResult.success(map);
    }


    /**
     * save role.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/role/saveRole")
    public AjaxResult saveRole(@RequestBody final RoleDTO dto) {
        Role role = new Role();
        role.setTimeUpdated(new Date());
        //role.setCompetence(StringUtils.isNotEmpty(dto.getCompetence()) ? dto.getCompetence() : "[]");
        role.setRole(dto.getRole());
        if (Objects.nonNull(dto.getId())) {
            role.setId(dto.getId());
            roleService.updateRole(role);
        } else {
            if (StringUtils.isNotEmpty(dto.getRole()) && roleService.checkExist(dto.getRole())) {
                LogUtils.error(LOGGER, () -> role.getRole() + "角色名称重复");
                return AjaxResult.error("角色名称重复");
            }
            role.setTimeCreated(new Date());
            roleService.saveRole(role);
        }
        return AjaxResult.success(true);
    }


    /**
     * delete rule.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/role/deleteRole")
    public AjaxResult deleteRole(@RequestBody final RoleDTO dto) {
        String[] ids = dto.getIds();
        Arrays.stream(ids)
                .filter(id -> roleService.checkRawData(id) == true)
                .forEach(x -> roleService.deleteRole(Integer.parseInt(x)));
        return AjaxResult.success(true);
    }


    /**
     * get rule list by selector.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/role/rolePageList")
    public AjaxResult ruleList(@RequestBody final RoleDTO dto) {
        ResponseResult<Role> ruleResult;
        ruleResult = roleService.getPageList(dto);
        return AjaxResult.success(ruleResult);
    }


    /**
     * save competence.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/role/saveCompetence")
    public AjaxResult saveCompetence(@RequestBody final RoleDTO dto) {
        //
        if (StringUtils.isNotEmpty(dto.getCompetence())) {
            //first to delete role_menu data
            menuService.deleteCompetenceByRoleId(dto.getId());

            String[] idList = dto.getCompetence().split(",");
            Arrays.stream(idList).forEach(id -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(Long.parseLong(id));
                roleMenu.setRoleId(dto.getId());
                menuService.saveCompetence(roleMenu);
            });
        }
        return AjaxResult.success(true);
    }
}

