package com.escredit.base.util.reflect;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 重写copyProperties, 忽略大小写
 */
public class BeanUtils {

    /**
     * 忽略大小写
     * 忽略下划线 _
     * 忽略空值
     *
     * 可构造重载方法, 设置可选参数
     */
    public static <T> T copyPropertiesIgnoreCase(Object source, Object target) {
        Map<String, Field> sourceMap = CacheFieldMap.getFieldMap(source.getClass());
        CacheFieldMap.getFieldMap(target.getClass()).values().forEach((it) -> {
            //转小写并忽略下划线
            Field field = sourceMap.get(it.getName().toLowerCase().replace("_", ""));
            if (field != null) {
                it.setAccessible(true);
                field.setAccessible(true);
                try {
                    //忽略null和空字符串
                    if (field.get(source) != null && StringUtils.isBlank(field.get(source).toString())) {
                        it.set(target, field.get(source));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("copyPropertiesIgnoreCase fail");
                }
            }
        });
        System.out.println(target.toString());
        return (T) target;
    }

    private static class CacheFieldMap {

        //map, 支持多个类型同时转换
        private static Map<String, Map<String, Field>> cacheMap = new HashMap<>();

        private static Map<String, Field> getFieldMap(Class clazz) {

            Map<String, Field> result = cacheMap.get(clazz.getName());
            if (result == null) {

                //控制同类型并发
                synchronized (CacheFieldMap.class) {
                    if (result == null) {
                        Map<String, Field> fieldMap = new HashMap<>();
                        for (Field field : clazz.getDeclaredFields()) {
                            //转小写并忽略下划线
                            fieldMap.put(field.getName().toLowerCase().replace("_", ""), field);
                        }
                        cacheMap.put(clazz.getName(), fieldMap);
                        result = cacheMap.get(clazz.getName());
                    }
                }
            }
            return result;
        }
    }
}