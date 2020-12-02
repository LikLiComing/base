package com.escredit.base.util.reflect;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * map转bean, 忽略大小写
 *
 * @author xuwucheng
 * @date 2020/8/3 17:07
 */
public class MapToBeanUtils {


    public Object mapToBean(Map<String, Object> map, Object obj) {

        //忽略key大小写
        CaseInsensitiveMap caseInsensitiveMap = new CaseInsensitiveMap(map);

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        //获取所有属性
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();


        for (PropertyDescriptor prop: properties) {

            String key = prop.getName();

            if(caseInsensitiveMap.containsKey(key) && caseInsensitiveMap.get(key) != null){

                try{
                    Object value = caseInsensitiveMap.get(key);
                    Method setMethod = prop.getWriteMethod();
                    setMethod.invoke(obj,value);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new RuntimeException("mapToBean fail");
                }
            }
        }

        return obj;
    }

}
