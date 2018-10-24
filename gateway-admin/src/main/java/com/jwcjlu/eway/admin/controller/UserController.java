
package com.jwcjlu.gateway.admin.controller;

import com.alibaba.fastjson.JSON;
import com.jwcjlu.gateway.admin.service.MenuService;
import com.jwcjlu.gateway.admin.service.UserService;
import com.jwcjlu.gateway.api.dto.TreeDTO;
import com.jwcjlu.gateway.api.dto.UserDTO;
import com.jwcjlu.gateway.api.entity.DashboardUser;
import com.jwcjlu.gateway.api.vo.ResponseResult;
import com.jwcjlu.gateway.common.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.UUIDUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * <p>Description: .</p>
 *
 * @author chengchuantuo
 */

@RestController
@RequestMapping("/gateway")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MenuService menuService;


    /**
     * logout.
     *
     * @param userDTO package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/logout")
    public AjaxResult logout(@RequestBody final UserDTO userDTO) {

/*Optional.ofNullable(userDTO.getToken()).ifPresent(x ->
                Optional.ofNullable(redisTemplate.opsForValue().get(x)).ifPresent(userName -> {
                    redisTemplate.delete(userName + "_competence");
                    redisTemplate.delete(x);
                })
        );*/

        return AjaxResult.success(true);
    }


    /**
     * login.
     *
     * @param userDTO package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/login")
    public AjaxResult login(@RequestBody final UserDTO userDTO) {
        DashboardUser user = userService.login(userDTO);
        if (Objects.nonNull(user.getId()) && user.getEnabled() == (byte) 1) {
            String token = UUIDUtils.random().toString();
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setToken(token);
            Map<String, Boolean> map = menuService.getMenuIdByRoleId(user.getRole());
            List<TreeDTO> treeList = menuService.getRoleCompetence();
            newUserDTO.setTreeData(treeList);
            newUserDTO.setCompetence(map);
            newUserDTO.setRole(user.getRole());
            newUserDTO.setUserName(user.getUserName());
            newUserDTO.setId(user.getId());
            //拼接权限
            Map<String, Object> authorize = menuService.getRoleMenus(user.getRole());
            redisTemplate.opsForValue().set(token, user.getRole(), 43200, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(user.getRole() + "_authorize", JSON.toJSONString(authorize), 86400, TimeUnit.SECONDS);
            //简易处理
            return AjaxResult.success(newUserDTO);
        }
        return AjaxResult.error("用户名或密码验证错误，请重新登录");
    }


    /**
     * get user list.
     *
     * @param user package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/user/userList")
    public AjaxResult getUserList(@RequestBody final UserDTO user) {
        ResponseResult<DashboardUser> userResult;
        if (StringUtils.isEmpty(user.getUserName())) {
            userResult = userService.getList(user);
        } else {
            userResult = userService.findByName(user);
        }
        return AjaxResult.success(userResult);
    }


    /**
     * delete user.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/user/remove")
    public AjaxResult delete(@RequestBody final UserDTO dto) {
        String[] ids = dto.getIds();
        Arrays.stream(ids).filter(x -> userService.checkRawData(x)).forEach(x -> userService.deleteUser(Integer.valueOf(x)));
        return AjaxResult.success(true);
    }


    /**
     * add / update user.
     *
     * @param dto package objects in order to get param
     * @return com.jwcjlu.skyway.common.result.AjaxResult
     */

    @PostMapping("/user/update")
    public AjaxResult update(@RequestBody final UserDTO dto) {
        //新增
        Calendar calendar = Calendar.getInstance();
        DashboardUser user = new DashboardUser();
        user.setId(Objects.nonNull(dto.getId()) ? Long.valueOf(dto.getId()) : null);
        user.setPassword(dto.getPassword());
        user.setUserName(dto.getUserName());
        user.setDateUpdated(calendar.getTime());
        user.setRole(dto.getRole());
        if (Objects.isNull(user.getId())) {
            //判断重名
            if (Objects.nonNull(userService.getUserByName(user.getUserName()).getId())) {
                return AjaxResult.error("姓名重复,请重新输入");
            }
            user.setDateCreated(calendar.getTime());
            user.setEnabled((byte) 1);
            userService.inserUser(user);
        } else {
            userService.updateUser(user);
        }
        return AjaxResult.success(dto);
    }

}

