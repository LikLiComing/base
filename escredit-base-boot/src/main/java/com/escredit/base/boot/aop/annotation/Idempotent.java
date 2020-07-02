package com.escredit.base.boot.aop.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * 启动幂等性功能
     */
    boolean enable() default true;
}
