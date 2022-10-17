package com.zhang.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
/**
 * 自定义Redis的序列化
 *
 * 重写serialize()方法，用于解决Java将数据通过RedisTemplate传入Redis时数据非String类型的情况导致报错的问题
 * 虽然Redis是非关系型数据库，但是RedisTemplate只支持传入String类型，所以我们在此次进行一次转换，将传入的Object类型转换为JSON字符串类型（即String类型），这样我们就可以使用了。
 * 此类的作用是在使用RedisTemplate调用Redis时，默认的参数是RedisTemplate<String,String>，我们将其改为RedisTemplate<String,Object>
 * ，在序列化传入时加一个判断，把Object转换成JSON，本质上还是使用的默认序列化方式，只不过加上了一个Object到JSON转换的过程
 * ------------
 * 实际上我们可以不需要这样操作，因为我们可以在调用时，把传入的key-value全部以转换为JSON格式后传入，这样就可以使用默认的RedisTemplate了
 *  实际开发中，数据的传递都是使用JSON的，所以此类和直接在传入时就传JSON实际上是殊途同归，只不过把这个步骤滞后了。
 */
public class MyStringRedisSerializer implements RedisSerializer<Object> {
    private final Charset charset;

    public MyStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public MyStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    /**
     * 我们修改此方法
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        // 判断：如果是String类型，则传入并使其序列化
        if (object instanceof String) {
            return object.toString().getBytes(charset);
        } else { // 如果不是String类型的数据，则使用fastjson来将其转换成String类型再传入
            String string = JSON.toJSONString(object);
            return string.getBytes(charset);
        }
    }
}
