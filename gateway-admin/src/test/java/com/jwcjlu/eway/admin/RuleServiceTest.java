/*
package com.jwcjlu.gateway.admin;


import RuleService;
import ApiDTO;
import Rule;
import ResponseResult;
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
 * <p>Description:</p>
 *
 * @author xiaoyu(Myth)
 * @version 1.0
 * @date 2018/4/4 9:16
 * @since JDK 1.8
 *//*

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureWebTestClient(timeout = "36000")
public class RuleServiceTest {

    @Autowired
    private RuleService ruleService;

    @Test
    public void RuleServiceTest(){
        ApiDTO dto = new ApiDTO();
        dto.setId("123");
        ResponseResult<Rule> list = ruleService.findBySid(dto);
        System.out.println(list.getDataList());
    }
}
*/
