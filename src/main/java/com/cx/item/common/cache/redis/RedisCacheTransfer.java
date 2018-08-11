package com.cx.item.common.cache.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * 使用中间类解决RedisCache.jedisConnectionFactory的静态注入，从而使MyBatis实现第三方缓存
 * Created by hwm on 2018/6/29.
 */
public class RedisCacheTransfer {

    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisMybatisCache.setJedisConnectionFactory(jedisConnectionFactory);
    }

    public void setRedisCacheMysqlHashKey(String redisCacheMysqlHashKey){
        RedisMybatisCache.setRedisCacheMysqlHashKey(redisCacheMysqlHashKey);
    }
}
