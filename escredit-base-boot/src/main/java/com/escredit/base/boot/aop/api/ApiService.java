package com.escredit.base.boot.aop.api;

import com.escredit.base.entity.DTO;

public abstract class ApiService {

    /**
     * 校验接口访问权限-业务数据有效性
     * @return
     */
   public abstract DTO checkPermission();

    /**
     * 设置token
     * 用于接口幂等性
     * @param token
     */
   public void set(String token){

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
