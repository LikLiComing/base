package com.escredit.base.boot.aop.idempotent;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等性
 */
@ConfigurationProperties(prefix = "escredit.base.boot.idempotent")
public class IdempotentProperties {
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
