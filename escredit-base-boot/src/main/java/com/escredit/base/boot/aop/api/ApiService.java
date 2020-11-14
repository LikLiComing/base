package com.escredit.base.boot.aop.api;

import com.escredit.base.entity.DTO;

public abstract class ApiService {

    /**
     * 校验接口访问权限-业务数据有效性
     * 默认方法
     * @return
     */
   public DTO checkPermission(){
       return new DTO(true);
   }

    /**
     * 设置token
     * 用于接口幂等性
     * @param token
     */
   public void set(String token){

   }

    /**
     * 用于计算接口限流
     * @param key
     * @param limit
     */
   public DTO limit(String key, ApiProperties.Limit limit){
       return new DTO(true);
   }

    /**
     * 删除及校验token
     * 用于接口幂等性
     * @param token
     * @return
     */
    public Boolean delete(String token){
       return false;
    }
}
