/*
package com.jwcjlu.gateway.admin.serviceTest;

import AppAuthService;
import AppAuth;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

*/
/**
 * @author chengchuantuo
 * @description: this is a test class .
 * @date 2018-05-16 17:29
 **//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class AppAuthServiceTest {

    @Autowired
    private AppAuthService appAuthService;

    @Test
    public void getAppList() {
        List<AppAuth> responseResult = appAuthService.getList();
        System.out.println(responseResult);
    }

    @Test
    public void saveOrUpdateAppAuth() {
        AppAuth appAuth = new AppAuth();
        appAuth.setAppKey("test");
        appAuth.setAppSecret("WDFGHJKL");
        appAuth.setDateCreated(new Date());
        //appAuth.setId((long)3);
        appAuth.setDateUpdated(new Date());
        appAuth.setEnabled((byte) 0);
        System.out.println(appAuthService.insertAppAuth(appAuth));
    }

    @Test
    public void deleteAppAuth() {
        appAuthService.deleteAppAuth((long)5);
    }
}
*/
