package com.zhang.config;

import com.zhang.serializer.MyStringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        //
        template.setConnectionFactory(redisConnectionFactory);
        // 指定Key的序列化方式是默认
        template.setKeySerializer(new StringRedisSerializer());
        // 指定Value的序列化方式是我们自己定义的
        template.setValueSerializer(new MyStringRedisSerializer());
        // 指定HashKey的序列化方式是默认
        template.setHashKeySerializer(new StringRedisSerializer());
        // 指定HashValue的序列化方式是我们自己定义的
        template.setHashValueSerializer(new MyStringRedisSerializer());

        return template;
    }
}
