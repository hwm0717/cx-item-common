package com.cx.item.common.cache.redis;

/**
 * 保存在redis里面的项目key前缀，每个项目前添加一个00x开始的前缀，防止项目过多，redis key造成冲突
 * Created by hwm on 2018/7/11.
 */
public class RedisCacheKeyPrefixNumber {

    /**
     * 阳光大姐微信小程序的redis前缀key
     */
    public static String item001 = "001";

    /**
     * 橙蟹报销小程序的redis前缀key
     */
    public static String item002 = "002";
}
