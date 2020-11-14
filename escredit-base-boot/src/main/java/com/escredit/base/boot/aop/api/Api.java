package com.escredit.base.boot.aop.api;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    /**
     * 启动幂等性功能
     */
    boolean idempotent() default false;

    /**
     * 启动数据校验
     * @return
     */
    boolean permission() default false;

    /**
     * 启动限流
     * @return
     */
    boolean limit() default false;

    /**
     * ApiService实现类
     * @return
     */
    Class<?> apiServiceImpl();

    /**
     * 数据校验中的执行方法
     * 默认是checkPermission，可定义多个
     * @return
     */
    String[] permissionMethods() default {"checkPermission"};

}
