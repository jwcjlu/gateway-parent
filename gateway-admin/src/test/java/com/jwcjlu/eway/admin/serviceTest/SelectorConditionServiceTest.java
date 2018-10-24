/*
package com.jwcjlu.gateway.admin.serviceTest;

import com.jwcjlu.gateway.admin.service.SelectorConditionService;
import ApiDTO;
import com.jwcjlu.gateway.api.entity.SelectorCondition;
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
 * @date 2018-05-16 17:30
 **//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class SelectorConditionServiceTest {

    @Autowired
    private SelectorConditionService conditionService;

    @Test
    public void getListTest(){
        ApiDTO dto = new ApiDTO();
        dto.setId("30");
        List<SelectorCondition> list = conditionService.findBySid(dto);
        System.out.println(list);
    }

    @Test
    public void addConditionTest(){
        SelectorCondition condition = new SelectorCondition();
        condition.setDateUpdated(new Date());
        condition.setDateCreated(new Date());
        condition.setParamType("url");
        condition.setOperator("=");
        condition.setParamName("test");
        condition.setParamValue("123");
        condition.setSelectorId("30");
        conditionService.insertSelectorCondition(condition);
    }

    @Test
    public void deleteConditionTest(){
        conditionService.deleteSelectorCondition(23);
    }
}
*/
