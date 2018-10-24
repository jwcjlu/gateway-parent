/*
package com.jwcjlu.gateway.admin.serviceTest;

import EnumService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class EnumServiceTest {

    @Autowired
    private EnumService numService;

    @Test
    public void getList() {
        List<String> pluginList = numService.getPluginTypeList();
        System.out.println(pluginList);
        List<String> matchModeList = numService.getMatchModeList();
        System.out.println(matchModeList);
        List<String> operatorTypeList = numService.getOperatorTypeList();
        System.out.println(operatorTypeList);
        List<String> paramTypeList = numService.getParamTypeList();
        System.out.println(paramTypeList);
        List<String> balanceTypeList = numService.getLoadBalanceTypeList();
        System.out.println(balanceTypeList);
    }
}
*/
