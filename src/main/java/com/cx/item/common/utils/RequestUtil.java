package com.cx.item.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求工具类
 */
public class RequestUtil {

    private static Logger log = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 验证用户请求是否重复请求
     * @param userToken 用户唯一表示
     * @param prefixKey 前缀key
     * @param redisTemplate
     * @param httpRequest
     * @return 重复请求返回true，否则false
     */
    public static boolean validateUserRequestIsRepeat(String userToken, String prefixKey, StringRedisTemplate redisTemplate, HttpServletRequest httpRequest){

        // get不判断重复
        String method = httpRequest.getMethod();
        if ("GET".equalsIgnoreCase(method)){
            return false;
        }

        Map<String, String> paramMap = ServletUtil.getParamMap(httpRequest);
        String reqPar = MapUtil.join(paramMap, ",", "=");

        // 过滤重复请求，每个token每个url+参数，一秒之内只能请求一次，防止重复提交
        // 解决方案，prefixKey+拼装用户token+请求url+参数，存入redis,每次请求判断redis该请求是否存在（有效时间一秒），如果存在，则重复提交，不做任何处理
        String filterUrl = StrUtil.format("{}_{}_{}_{}", prefixKey, userToken, httpRequest.getRequestURI(), reqPar);
        if (redisTemplate.hasKey(filterUrl)){
            log.debug("============================用户重复请求，返回true===========================");
            return true;
        }else {
            redisTemplate.opsForValue().set(filterUrl, httpRequest.getRequestURI(), 1, TimeUnit.SECONDS);
            return false;
        }
    }
}
