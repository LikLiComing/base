package com.escredit.base.boot.shiro.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.escredit.base.boot.shiro.ShiroProperties;
import com.escredit.base.boot.shiro.service.SecurityService;
import com.escredit.base.util.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

public class JwtUtil {

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 获取属性
     * @param token
     * @param name 比如 tel id
     * @return
     */
    public static String getClainAsString(String token,String name) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(name).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取对象
     * 由于签名都简化成string类型，所以目前只支持设置string属性
     * @param token
     * @param cls
     * @return
     */
    public static Object getObject(String token,Class cls){
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> mapClaim = jwt.getClaims();
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        Set<Field> fieldSet = ObjectUtils.getClassAllFields(obj.getClass());
        for(Field field:fieldSet){
            String fieldName = field.getName();
            Claim valueClaim = mapClaim.get(fieldName);
            if(valueClaim == null){
                continue;
            }
            field.setAccessible(true);
            Object value = null;
            try {
                if(field.getType() == String.class){
                    value = valueClaim.asString();
                }else if(field.getType() == List.class){
                    //claim里面存放的是数组
                    value = Arrays.asList(valueClaim.asArray(String.class));
                }
              field.set(obj,value);
            } catch (Exception e) {
            }
        }
        return obj;
    }

    /**
     * 获取jwt 签名
     * @param obj 被加入签名的对象
     * @param secret 用于算法密钥，一般采用用户密码
     * @param expireTime 过期时间
     * @return
     */
    public static String sign(Object obj,String secret,Long expireTime) {
        Map map = ObjectUtils.toMap(obj);
        Set<Map.Entry<String, Object>> entrys = map.entrySet();
        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, Object> entry : entrys) {
            Object value = entry.getValue();
                if(!(value instanceof Collection)){
                    builder.withClaim(entry.getKey(),String.valueOf(value));
                }else{
                    try {
                        Collection<String> valueList = (Collection) value;
                        String[] valueArray = valueList.toArray(new String[valueList.size()]);
//                        if(valueList.size() > 0 && valueList.get(0) instanceof String){
                        builder.withArrayClaim(entry.getKey(), valueArray);
//                        }
                    } catch (Exception e) {
                    }
                }
        }

        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return builder.withExpiresAt(date).sign(algorithm);
    }

}
