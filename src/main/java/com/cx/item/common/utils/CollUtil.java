package com.cx.item.common.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 集合工具类
 */
public class CollUtil {

    /**
     * 将collection集合转换成map集合，map->key=keyValue指定值，value=集合对象
     *
     * @param keyValue map key
     * @param coll     需要转换集合
     * @return
     */
    public static <T> Map<String, T> collToMapKey(String keyValue, Collection<T> coll) {

        Map<String, T> resultMap = new HashMap();
        if (coll == null || coll.size() <= 0) {
            return resultMap;
        }

        String getMethodName = "get" + keyValue.substring(0, 1).toUpperCase()
                + keyValue.substring(1);

        for (T t : coll) {
            try {
                Method getMethod = t.getClass().getMethod(getMethodName,
                        new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});

                if (value != null) {
                    resultMap.put(String.valueOf(value), t);
                }
            } catch (Exception e) {
            }
        }

        return resultMap;
    }
}
