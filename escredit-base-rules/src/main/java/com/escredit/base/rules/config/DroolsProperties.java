package com.escredit.base.rules.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "escredit.base.rules.drools")
public class DroolsProperties {

    private boolean enable;

    /**
     * 规则文件和决策表目录，多个目录使用逗号分割
     */
    private String[] path = {"rules/"};

    /**
     * 是否打印调试
     */
    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }
}
