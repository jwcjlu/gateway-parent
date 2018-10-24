/*
package com.jwcjlu.gateway.admin.serviceTest;

import com.jwcjlu.gateway.admin.service.RuleConditionService;
import ApiDTO;
import com.jwcjlu.gateway.api.entity.RuleCondition;
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
 * @date 2018-05-16 17:30
 **//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class RuleConditionServiceTest {

    @Autowired
    private RuleConditionService conditionService;

    @Test
    public void getListTest(){
        ApiDTO dto = new ApiDTO();
        dto.setId("30");
        conditionService.findBySid(dto);
    }

    @Test
    public void addConditionTest(){
        RuleCondition condition = new RuleCondition();
        condition.setDateUpdated(new Date());
        condition.setDateCreated(new Date());
        condition.setParamType("url");
        condition.setOperator("=");
        condition.setParamName("test");
        condition.setParamValue("123");
        condition.setRuleId((long)30);
        conditionService.insertRuleCondition(condition);
    }

    @Test
    public void deleteConditionTest(){
        conditionService.deleteRuleCondition(17);
    }
}
*/
