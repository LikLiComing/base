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

        //遍历target-Field
        CacheFieldMap.getFieldMap(target.getClass()).values().forEach((it) -> {

            //转小写并忽略下划线, 取source-Field
            Field field = sourceMap.get(it.getName().toLowerCase().replace("_", ""));

            if (field != null) {

                it.setAccessible(true);
                field.setAccessible(true);

                try {
                    //忽略null和空字符串, source空值不覆盖target值
                    if (null != field.get(source) && StringUtils.isNotEmpty(field.get(source).toString())) {
                        //赋值
                        it.set(target, field.get(source));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("copyPropertiesIgnoreCase fail");
                }
            }
        });

        return (T) target;
    }

    private static class CacheFieldMap {

        //map缓存, 同类型只转换一次
        private static Map<String, Map<String, Field>> cacheMap = new HashMap<>();

        private static Map<String, Field> getFieldMap(Class clazz) {

            Map<String, Field> result = cacheMap.get(clazz.getName());
            if (result == null) {

                //控制同类型并发
                synchronized (CacheFieldMap.class) {
                    if (result == null) {
                        Map<String, Field> fieldMap = new HashMap<>();
                        for (Field field : clazz.getDeclaredFields()) {
                            //转小写并忽略下划线, 存Field
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