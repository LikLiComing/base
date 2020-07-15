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
     * ApiService实现类
     * @return
     */
    Class<?> apiServiceImpl();

}
