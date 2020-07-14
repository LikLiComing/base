/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.escredit.base.util.lang;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * @author ThinkGem
 * @version 2018-08-11
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(final Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return NumberUtils.toDouble(StringUtils.trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(final Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(final Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(final Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字符串
	 * @param obj
	 * @return
	 */
	public static String toString(final Object obj) {
		return toString(obj, StringUtils.EMPTY);
	}

	/**
	 * 如果对象为空，则使用defaultVal值
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 空转空字符串（"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""）
	 * @param val 需转换的值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val) {
		return ObjectUtils.toStringIgnoreNull(val, StringUtils.EMPTY);
	}

	/**
	 * 空对象转空字符串 （"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal）
	 * @param val 需转换的值
	 * @param defaultVal 默认值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val, String defaultVal) {
		String str = ObjectUtils.toString(val);
		return !"".equals(str) && !"null".equals(str.trim().toLowerCase()) ? str : defaultVal;
	}

	/**
	 * 注解到对象复制，只复制能匹配上的方法。 硕正组件用。
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(Object annotation, Object object) {
		if (annotation != null && object != null) {
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()) {
				if (StringUtils.startsWith(m.getName(), "set")) {
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())) {
//							if (object == null){
//								object = objectClass.newInstance();
//							}
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						// 忽略所有设置失败方法
					}
				}
			}
		}
	}

	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		if (object == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		byte[] bytes = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Serialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
		return bytes;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		Object object = null;
		if (bytes.length > 0) {
			try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
					ObjectInputStream ois = new ObjectInputStream(bais);) {
				object = ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Unserialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
		return object;
	}


	/**
	 * 将Object对象里面的属性和值转化成Map对象
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> toMap(Object obj) {
		Map<String, Object> map = new HashMap<>();
		if(obj == null){
			return map;
		}
		Set<Field> set = getClassAllFields(obj.getClass());
		try {
			for (Field field : set) {
				field.setAccessible(true);
				String fieldName = field.getName();
				if(field.get(obj) == null){
					continue;
				}
				map.put(fieldName, field.get(obj));
			}
		} catch (Exception ex){
		}
		return map;
	}

	public static void setFieldValue(Object obj,Map fieldValueMap) {
		if(obj == null){
			return;
		}
		Class<?> clazz = obj.getClass();
		Set<Field> set = getClassAllFields(clazz);
		try {
			for (Field field : set) {
				field.setAccessible(true);
				String fieldName = field.getName();
				field.set(obj,fieldValueMap.get(fieldName));
			}
		} catch (Exception ex){
		}
	}

	public static Set<Field> getClassAllFields(Class clazz){
		Set<Field> allGenericFields = new HashSet<Field>();
		return getClassAllFields(clazz,allGenericFields);
	}

	public static Set<Field> getClassAllFields(Class clazz, Set<Field> allGenericFields) {
		// 如果clazz为空则直接返回
		if (clazz == null) {
			return allGenericFields;
		}

		Object parent = clazz.getGenericSuperclass();
		// 如果有父类并且父类不是Object 则递归调用
		if (parent != null && !((Class) parent).getName().equals("Object")) {
			getClassAllFields((Class) parent,allGenericFields);
		}

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++){
				allGenericFields.add(fields[i]);
			}
		}
		// 存在父类则递归调用
		return allGenericFields;
	}


}
