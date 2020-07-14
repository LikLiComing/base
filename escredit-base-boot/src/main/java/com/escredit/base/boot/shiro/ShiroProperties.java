package com.escredit.base.boot.shiro;

/**
 * Created by liyongping on 2020/7/13 3:46 PM
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * shiro
 */
@Component
@ConfigurationProperties(prefix = "escredit.base.boot.shiro")
public class ShiroProperties {

    /**
     * shiro总开关
     */
    private boolean enable;

    /**
     * shiro的登录url
     */
    private String loginUrl = "/admin/user/login/tel";

    private Jwt jwt = new Jwt();

    private Code code = new Code();

    private Api api = new Api();

    /**
     * shiro的过滤属性，格式为"key,value"
     */
    private List<String> filterChainDefinitionList;

    /**
     * 验证码
     */
    public static class Code{

        private boolean enable;

        /**
         * 实现securityService中验证部分的类别名
         * todo 目前写的固定值
         */
        private String serviceName = "codeSecurityService";

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    public static class Jwt{

        private boolean enable;

        /**
         * 用于验证的token名
         */
        private String tokenName = "authhead";

        /**
         * 默认的token value，用于测试
         */
        private String tokenValue;

        /**
         * 过期时间
         */
        private Long expireTime = 30 * 24 * 60 * 60 * 1000L;

        /**
         * 实现securityService中验证部分的类别名
         * todo 目前写的固定值
         */
        private String serviceName = "jwtSecurityService";

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getTokenValue() {
            return tokenValue;
        }

        public void setTokenValue(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }
    }

    /**
     * 对外开放API
     */
    public static class Api{

        private boolean enable;

        /**
         * 白名单
         */
        private String[] whiteUrl;

        /**
         * 授权的帐号
         */
        private String account;

        /**
         * 授权的密钥
         */
        private String key;

        /**
         * 做校验的签名参数名
         */
        private String signName = "sign";

        public String getSignName() {
            return signName;
        }

        public void setSignName(String signName) {
            this.signName = signName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String[] getWhiteUrl() {
            return whiteUrl;
        }

        public void setWhiteUrl(String[] whiteUrl) {
            this.whiteUrl = whiteUrl;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    public List<String> getFilterChainDefinitionList() {
        return filterChainDefinitionList;
    }

    public void setFilterChainDefinitionList(List<String> filterChainDefinitionList) {
        this.filterChainDefinitionList = filterChainDefinitionList;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }
}
