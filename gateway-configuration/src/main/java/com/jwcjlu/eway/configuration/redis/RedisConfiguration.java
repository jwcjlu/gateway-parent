package com.jwcjlu.gateway.configuration.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RedisConfiguration .
 * @author xiaoyu(Myth)
 */
@Configuration
public class RedisConfiguration {

    /**
     * Generator redis key .
     *
     * @return KeyGenerator
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            AtomicReference<StringBuilder> sb = new AtomicReference<>(new StringBuilder());
            sb.get().append(target.getClass().getName());
            sb.get().append(method.getName());
            return Arrays.stream(params)
                    .map(obj -> sb.get().append(obj.toString())).toString();

        };
    }

    /**
     * Bean define RedisTemplate.
     *
     * @param redisConnectionFactory {@linkplain RedisConnectionFactory}
     * @return RedisTemplate {@linkplain RedisTemplate}
     */
    @Bean
    @SuppressWarnings("unchecked")
    public RedisTemplate redisTemplate(final RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
