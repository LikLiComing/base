package com.escredit.base.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ChH
 * @create 2020/7/28
 */
@ConfigurationProperties(prefix = "spring.mail")
@Component
public class EmailProperties {

    private static final String defaultFtlFolder = "email";
    private static final String defaultFtlName = "default.ftl";

    private String username;
    private String ftlFolder = defaultFtlFolder;
    private String ftlName = defaultFtlName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFtlFolder() {
        return ftlFolder;
    }

    public void setFtlFolder(String ftlFolder) {
        this.ftlFolder = ftlFolder;
    }

    public String getFtlName() {
        return ftlName;
    }

    public void setFtlName(String ftlName) {
        this.ftlName = ftlName;
    }
}
