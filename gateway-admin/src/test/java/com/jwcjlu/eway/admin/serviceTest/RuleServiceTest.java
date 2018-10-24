/*
package com.jwcjlu.gateway.admin.serviceTest;

import RuleService;
import Rule;
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
public class RuleServiceTest {

    @Autowired
    private RuleService ruleService;

    @Test
    public void getRuleList(){
        List<Rule> list = ruleService.getList("F2YFL6gfBxRdqjUw");
        System.out.println();
    }

    @Test
    public void saveRuleTest(){
        Rule rule = new Rule();
        rule.setLoged((byte)0);
        rule.setEnabled((byte)0);
        rule.setSelectorId("1231");
        rule.setHandle("{}");
        rule.setMatchMode(0);
        rule.setRank(123);
        rule.setName("测试");
        rule.setDateUpdated(new Date());
        rule.setDateCreated(new Date());
        ruleService.insertRule(rule);
    }

    @Test
    public void updateRuleTest(){
        Rule rule = new Rule();
        rule.setId((long)32);
        rule.setLoged((byte)1);
        rule.setEnabled((byte)1);
        rule.setSelectorId("123");
        rule.setHandle("{1231231231}");
        rule.setMatchMode(0);
        rule.setRank(123);
        rule.setName("测试1");
        rule.setDateUpdated(new Date());
        rule.setDateCreated(new Date());
        ruleService.updateRule(rule);
    }

    @Test
    public void deleteRuleTest(){
        ruleService.deleteRule("31");
    }
}
*/
