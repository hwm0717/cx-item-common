package com.cx.item.common.cache.redis;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用redis做mybatis二级缓存
 * Created by hwm on 2018/6/29.
 */
public class RedisMybatisCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(RedisMybatisCache.class);

    private static JedisConnectionFactory jedisConnectionFactory;
    // redis使用二级缓存key前面添加的前缀，增删改时clear方法清空已改前缀开头的所有key，以防清空整个redis数据库，发生数据异常
    private static String redisCacheMysqlHashKey;
    private final String id;
    /**
     * The {@code ReadWriteLock}.
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisMybatisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }

    protected static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisMybatisCache.jedisConnectionFactory = jedisConnectionFactory;
    }

    protected static void setRedisCacheMysqlHashKey(String redisCacheMysqlHashKey){
        RedisMybatisCache.redisCacheMysqlHashKey = redisCacheMysqlHashKey;
    }

    @Override
    public void clear() {
        RedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection(); //连接清除数据
            connection.del(redisCacheMysqlHashKey.getBytes());
            logger.debug("=========================清除redis二级缓存数据hashKey=" + redisCacheMysqlHashKey);
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                logger.debug("Close RedisConnection");
                connection.close();
            }
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        RedisConnection connection = null;

        logger.debug(StrUtil.format("========================>getObject key=【{}】", key.toString()));

        try {
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(); //借用spring_data_redis.jar中的JdkSerializationRedisSerializer.class
            result = serializer.deserialize(connection.hGet(redisCacheMysqlHashKey.getBytes(), key.toString().getBytes()));
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                logger.debug("Close RedisConnection");
                connection.close();
            }
        }
        return result;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    @Override
    public int getSize() {
        int result = 0;
        RedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection();
            result = Integer.parseInt(connection.hLen(redisCacheMysqlHashKey.getBytes()).toString());
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                logger.debug("Close RedisConnection");
                connection.close();
            }
        }
        return result;
    }

    @Override
    public void putObject(Object key, Object value) {
        RedisConnection connection = null;
        try {
            logger.debug(StrUtil.format(">>>>>>>>>>>>>>>>>>>>>>>>putObject \n\tkey=【{}】\n\tvalue=【{}】", key.toString(), value));
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(); //借用spring_data_redis.jar中的JdkSerializationRedisSerializer.class

            connection.hSet(redisCacheMysqlHashKey.getBytes(), key.toString().getBytes(), serializer.serialize(value));

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                logger.debug("Close RedisConnection");
                connection.close();
            }
        }
    }

    @Override
    public Object removeObject(Object key) {
        RedisConnection connection = null;
        Object result = null;

        logger.debug(StrUtil.format("========================>removeObject key=【{}】", key.toString()));
        try {
            connection = jedisConnectionFactory.getConnection();
            result = connection.hDel(redisCacheMysqlHashKey.getBytes(), key.toString().getBytes());
        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                logger.debug("Close RedisConnection");
                connection.close();
            }
        }
        return result;
    }
}
