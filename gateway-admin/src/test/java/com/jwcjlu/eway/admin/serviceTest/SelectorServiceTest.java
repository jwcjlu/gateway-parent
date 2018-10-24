/*
package com.jwcjlu.gateway.admin.serviceTest;

import com.jwcjlu.gateway.admin.service.SelectorService;
import ApiDTO;
import com.jwcjlu.gateway.api.entity.Selector;
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
public class SelectorServiceTest {

    @Autowired
    private SelectorService selectorService;

    @Test
    public void getListTest(){
        ApiDTO dto = new ApiDTO();
        List<Selector> selectorList = selectorService.getList("global");
        System.out.println(selectorList);
    }

    @Test
    public void saveSelectorTest(){
        Selector selector = new Selector();
        selector.setDateUpdated(new Date());
        selector.setDateCreated(new Date());
        selector.setRank(100);
        selector.setName("测试");
        selector.setPluginId((long)8);
        selector.setMatchMode(1);
        selector.setLoged((byte)0);
        selector.setType((byte)0);
        selector.setEnabled((byte)0);
        selector.setContinued((byte)0);
        selector.setId("WERTERTYEFVDFG");
        selectorService.insertSelector(selector);
    }

    @Test
    public void updateSelectorTest(){
        Selector selector = new Selector();
        selector.setDateUpdated(new Date());
        selector.setDateCreated(new Date());
        selector.setRank(101);
        selector.setName("测试1");
        selector.setPluginId((long)8);
        selector.setMatchMode(2);
        selector.setLoged((byte)1);
        selector.setType((byte)1);
        selector.setEnabled((byte)1);
        selector.setContinued((byte)1);
        selector.setId("WERTERTYEFVDFG");
        selectorService.updateSelector(selector);
    }

    @Test
    public void deleteSelectorTest(){
        selectorService.deleteSelector("WERTERTYEFVDFG");
    }

}
*/
