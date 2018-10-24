/*
package com.jwcjlu.gateway.admin.redis;

import com.jwcjlu.gateway.admin.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

*/
/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 * @version 1.0
 * @date 2018/4/25 16:59
 * @since JDK 1.8
 *//*

@SuppressWarnings("unchecked")
public class RedisDataTest extends BaseTest {


    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testBuildPluginData() {

       */
/* PluginZkDTO divide = new PluginZkDTO();
        divide.setEnabled(Boolean.TRUE);
        divide.setId(1);
        divide.setName(PluginEnum.DIVIDE.getName());
        redisTemplate.opsForHash().put(RedisKeyConstants.PLUGIN, PluginEnum.DIVIDE.getName(), divide);

        PluginZkDTO monitor = new PluginZkDTO();
        monitor.setEnabled(Boolean.TRUE);
        monitor.setId(2);
        monitor.setName(PluginEnum.MONITOR.getName());

        redisTemplate.opsForHash().put(RedisKeyConstants.PLUGIN, PluginEnum.MONITOR.getName(), monitor);

        final Map<String, PluginZkDTO> map = redisTemplate.opsForHash().entries(RedisKeyConstants.PLUGIN);
        final List<PluginZkDTO> pluginZkDTOS = new ArrayList<>(map.values());
        System.out.println(pluginZkDTOS.toString());
        final PluginZkDTO dto = (PluginZkDTO) redisTemplate.opsForHash().get(RedisKeyConstants.PLUGIN, PluginEnum.MONITOR.getName());
        System.out.println(dto.toString());*//*

    }


    @Test
    public void testSelectorData(){

    }
}
*/
