package com.escredit.base.boot.documantation.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger
 */
@ConfigurationProperties(prefix = "escredit.base.boot.swagger")
public class SwaggerProperties {
    private boolean enable;

    private String tokenName = "authhead";

    private String tokenValue;

    private String title = "接口列表";

    private String description;

    private String version;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
