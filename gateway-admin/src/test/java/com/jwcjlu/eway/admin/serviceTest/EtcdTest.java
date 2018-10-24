package com.jwcjlu.gateway.admin.serviceTest;

import com.jwcjlu.gateway.admin.service.EtcdService;
import com.jwcjlu.gateway.api.entity.Plugin;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-08-14 11:32
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class EtcdTest {

    @Autowired
    private EtcdService etcdService;

    @Test
    public void loadPlugin(){
        Plugin plugin = new Plugin();
        plugin.setEnabled((byte)1);
        plugin.setPluginName("concurrency");
        plugin.setId(1L);
        etcdService.loadPlugin(plugin);
    }
}
