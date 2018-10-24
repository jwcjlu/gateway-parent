/*
package com.jwcjlu.gateway.admin.serviceTest;

import UserService;
import UserDTO;
import com.jwcjlu.gateway.api.entity.DashBoardUser;
import ResponseResult;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

*/
/**
 * @author chengchuantuo
 * @description: this is a test class .
 * @date 2018-05-16 17:31
 **//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getList(){
        UserDTO dto = new UserDTO();
        ResponseResult<DashBoardUser> responseResult = userService.getList(dto);
        System.out.println(responseResult);
    }

    @Test
    public void saveOrUpdateUser(){
        DashBoardUser user = new DashBoardUser();
        user.setUserName("成传拓Test");
        user.setPassword("123456");
        user.setRole(0);
        user.setDateCreated(new Date());
        //appAuth.setId((long)3);
        user.setDateUpdated(new Date());
        user.setEnabled((byte)0);
        userService.inserUser(user);
        user.setId((long)18);
        userService.updateUser(user);
    }

    @Test
    public void deleteAppAuth(){
       userService.deleteUser(18);
    }
}
*/
