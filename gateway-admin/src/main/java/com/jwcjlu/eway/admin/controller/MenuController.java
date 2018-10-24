
package com.jwcjlu.gateway.admin.controller;

import com.jwcjlu.gateway.admin.service.MenuService;
import com.jwcjlu.gateway.api.dto.MenuDTO;
import com.jwcjlu.gateway.api.entity.Menu;
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

import java.util.Arrays;
import java.util.Objects;

/**
 * this is a class .
 *
 * @author chengchuantuo
 * @date 2018-06-21 11:03
 **/

@RestController
@RequestMapping("/gateway")
public class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;


    /**
     * get rule list by selector.
     *
     * @param menuDTO package object to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/menu/menuList")
    public AjaxResult roleList(@RequestBody final MenuDTO menuDTO) {
        ResponseResult<Menu> result = menuService.getMenuPageList(menuDTO);
        return AjaxResult.success(result);
    }


    /**
     * save menu.
     *
     * @param menuDTO package object to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/menu/saveMenu")
    public AjaxResult saveMenu(@RequestBody final MenuDTO menuDTO) {
        if (Objects.isNull(menuDTO.getId()) || StringUtils.isEmpty(menuDTO.getName())) {
            return AjaxResult.error("必填参数为空，插入失败");
        }
        if (Objects.nonNull(menuService.getMenuById(menuDTO.getId()).getId())) {
            return AjaxResult.error("节点ID已经重复，请重新输入");
        }
        Menu parentMenu = menuService.getMenuById(menuDTO.getParentId());

        if (Objects.nonNull(menuDTO.getParentId()) && Objects.isNull(parentMenu.getId())) {
            return AjaxResult.error("父节点ID不存在，请重新输入");
        } else if (Objects.nonNull(menuDTO.getParentId())) {
            //更改父节点叶子节点状态
            if (parentMenu.getIsLeaf() == (byte) 0) {
                parentMenu.setIsLeaf((byte) 1);
                menuService.updateMenuById(parentMenu);
            }
        }
        Menu newMenu = new Menu();
        newMenu.setId(menuDTO.getId());
        newMenu.setIsLeaf((byte) 0);
        newMenu.setParentId(Objects.nonNull(menuDTO.getParentId()) ? menuDTO.getParentId() : -1);
        newMenu.setName(menuDTO.getName());
        newMenu.setUrl(menuDTO.getUrl());
        menuService.saveMenu(newMenu);

        return AjaxResult.success(newMenu);
    }


    /**
     * delete menu.
     *
     * @param menuDTO package object to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/menu/deleteMenu")
    public AjaxResult deleteMenu(@RequestBody final MenuDTO menuDTO) {
        String[] ids = menuDTO.getIds();
        Arrays.stream(ids).forEach(x -> {
            menuService.deleteMenu(Long.parseLong(x));
            LogUtils.debug(LOGGER, () -> x + "已被删除");
        });

        return AjaxResult.success(true);
    }
}

