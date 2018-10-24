/*
package com.jwcjlu.skyway.admin.serviceTest;

import com.jwcjlu.skyway.admin.service.ZkService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class ZkServiceTest {

    @Autowired
    private ZkService zkService;

    @Test
    public void loadTest() {
        zkService.loadSelector();
        zkService.loadPlugin();
    }

    @Test
    public void loadPluginTest() {
        zkService.loadPlugin("divide", false);
    }

}
*/
