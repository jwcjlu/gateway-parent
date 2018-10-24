
package com.jwcjlu.gateway.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jwcjlu.gateway.admin.mapper.MenuMapper;
import com.jwcjlu.gateway.admin.mapper.RoleMenuMapper;
import com.jwcjlu.gateway.admin.service.MenuService;
import com.jwcjlu.gateway.api.dto.MenuDTO;
import com.jwcjlu.gateway.api.dto.TreeDTO;
import com.jwcjlu.gateway.api.entity.Menu;
import com.jwcjlu.gateway.api.entity.MenuExample;
import com.jwcjlu.gateway.api.entity.RoleMenu;
import com.jwcjlu.gateway.api.entity.RoleMenuExample;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * menu service .
 *
 * @author chengchuantuo
 * @date 2018-06-19 17:53
 **/

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    private List<TreeDTO> tree;

    @Override
    public List<Menu> getMenuList() {
        MenuExample example = new MenuExample();
        example.createCriteria();
        example.setOrderByClause("id ASC");
        return menuMapper.selectByExample(example);
    }

    @Override
    public Map<String, Boolean> getMenuIdByRoleId(final Integer id) {
        Map<String, Boolean> menuId = new HashMap<>();
        RoleMenuExample example = new RoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(id);
        List<RoleMenu> list = roleMenuMapper.selectByExample(example);
        if (Objects.nonNull(list)) {
            list.forEach(roleMenu -> menuId.put(roleMenu.getMenuId().toString(), true));
        }
        return menuId;
    }

    @Override
    public List<TreeDTO> getRoleCompetence() {
        List<Menu> menuList = getMenuList();
        tree = new ArrayList<>();
        menuList.stream()
                .filter(x -> x.getParentId() == -1)
                .forEach(menu -> {
                    TreeDTO dto = new TreeDTO();
                    dto.setDisable(false);
                    dto.setLabel(menu.getName());
                    dto.setId(menu.getId());
                    tree.add(dto);
                });

        this.getChildren();
        return tree;
    }

    @Override
    public int deleteCompetenceByRoleId(final Integer roleId) {
        RoleMenuExample example = new RoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return roleMenuMapper.deleteByExample(example);
    }

    @Override
    public int saveCompetence(final RoleMenu roleMenu) {
        return roleMenuMapper.insertSelective(roleMenu);
    }

    @Override
    public List<RoleMenu> getCompetenceByRoleId(final Integer roleId) {
        RoleMenuExample example = new RoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return roleMenuMapper.selectByExample(example);
    }

    @Override
    public Map<String, Object> getRoleMenus(final Integer roleId) {
        RoleMenuExample example = new RoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleMenu> roleMenuList = roleMenuMapper.selectByExample(example);
        Map<String, Object> map = new HashMap<>();
        if (Objects.nonNull(roleMenuList)) {
            roleMenuList.forEach(roleMenu -> {
                Menu menu = menuMapper.selectByPrimaryKey(roleMenu.getMenuId());
                if (Objects.nonNull(menu)) {
                    String url = menu.getUrl();
                    map.put(url, true);
                }
            });
        }
        return map;
    }

    @Override
    public int saveMenu(final Menu menu) {
        return menuMapper.insertSelective(menu);

    }

    @Override
    public int deleteMenu(final Long id) {
        return menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ResponseResult<Menu> getMenuPageList(final MenuDTO dto) {
        MenuExample example = new MenuExample();
        if (Objects.nonNull(dto.getParentId()) && Objects.nonNull(dto.getName())) {
            example.createCriteria().andParentIdEqualTo(dto.getParentId()).andNameLike("%" + dto.getName() + "%");
        } else if (Objects.nonNull(dto.getName())) {
            example.createCriteria().andNameLike("%" + dto.getName() + "%");
        } else if (Objects.nonNull(dto.getParentId())) {
            example.createCriteria().andParentIdEqualTo(dto.getParentId());
        } else {
            example.createCriteria();
        }
        example.setOrderByClause("id ASC");
        Integer pageNumber = dto.getPageNumber();
        Integer pageSize = dto.getPageSize();
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageNumber = 1;
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Menu> list = menuMapper.selectByExample(example);
        PageInfo page = new PageInfo(list);
        ResponseResult<Menu> result = new ResponseResult<>();
        result.setTotalCount(page.getTotal());
        result.setDataList(list);
        return result;
    }

    @Override
    public Menu getMenuById(final Long id) {
        Menu menu = menuMapper.selectByPrimaryKey(id);
        if (Objects.nonNull(menu)) {
            return menu;
        }
        return new Menu();
    }

    @Override
    public int updateMenuById(final Menu menu) {
        return menuMapper.updateByPrimaryKey(menu);
    }

    private void getChildren() {
        tree.forEach(dto -> getTreeDto(dto));
        tree.forEach(dto -> {
            if (Objects.nonNull(dto.getChildren())) {
                dto.getChildren().forEach(children -> getTreeDto(children));
            }
        });
    }

    private TreeDTO getTreeDto(final TreeDTO treeDTO) {
        //TreeDTO dto = treeDTO;
        MenuExample example = new MenuExample();
        example.createCriteria().andParentIdEqualTo(treeDTO.getId());
        List<Menu> list = menuMapper.selectByExample(example);
        list.forEach(menu -> {
            TreeDTO treeDto = new TreeDTO();
            treeDto.setId(menu.getId());
            treeDto.setLabel(menu.getName());
            treeDto.setDisable(false);
            if (Objects.nonNull(treeDTO.getChildren())) {
                treeDTO.getChildren().add(treeDto);
            } else {
                List<TreeDTO> treeList = new ArrayList<>();
                treeList.add(treeDto);
                treeDTO.setChildren(treeList);
            }
        });
        return treeDTO;
    }
}


