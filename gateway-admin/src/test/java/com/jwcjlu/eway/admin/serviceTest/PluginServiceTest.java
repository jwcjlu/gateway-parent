/*
package com.jwcjlu.gateway.admin.serviceTest;

import PluginService;
import ApiDTO;
import Plugin;
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
 * @date 2018-05-16 17:29
 **//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class PluginServiceTest {

    @Autowired
    private PluginService pluginService;

    @Test
    public void getListTest() {
        ApiDTO apiDTO = new ApiDTO();
        pluginService.getList(apiDTO);
    }

    @Test
    public void changeStatusTest() {
        pluginService.changePluginStatus("8", (byte) 0);
    }

    @Test
    public void getStatusTest() {
        pluginService.getPluginStatus("8");
    }

    @Test
    public void updatePluginTest() {
        Plugin plugin = new Plugin();
        plugin.setName("测试2");
        plugin.setEnable((byte) 1);
        plugin.setDateCreated(new Date());
        plugin.setDateCreated(new Date());
        plugin.setId((long) 15);
        pluginService.updatePlugin(plugin);
    }

    @Test
    public void savePluginTest() {
        Plugin plugin = new Plugin();
        plugin.setName("测试1");
        plugin.setEnable((byte) 0);
        plugin.setDateCreated(new Date());
        plugin.setDateCreated(new Date());
        pluginService.insertPlugin(plugin);
    }

    @Test
    public void deletePluginTest() {
        pluginService.deletePlugin((long)15);
    }
}
*/
