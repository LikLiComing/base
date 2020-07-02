package com.escredit.base.boot.aop.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "escredit.base.boot.log")
public class LogAopProperties {
    /**
     * 组件开关
     */
    private boolean enable;
    /**
     * 是否开启debug模式
     */
    private boolean debug;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
