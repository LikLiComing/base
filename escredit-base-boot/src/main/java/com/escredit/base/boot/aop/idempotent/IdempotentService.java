package com.escredit.base.boot.aop.idempotent;

/**
 * 一般通过redis实现
 */
public interface IdempotentService {
    void set(String token);

    Boolean delete(String token);
}
