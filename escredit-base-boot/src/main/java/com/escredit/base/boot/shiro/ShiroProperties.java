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

    /**
     * 登录成功后的url
     */
    private String successUrl = "/admin/main/mainPage";

    private Password password = new Password();

    private Jwt jwt = new Jwt();

    private Code code = new Code();

    private Api api = new Api();

    private CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();

    private EhCacheManager ehCacheManager = new EhCacheManager();

    /**
     * shiro的过滤属性，格式为"key,value"
     */
    private List<String> filterChainDefinitionList;

    /**
     * 是否采用cglib代理方式，从而解决shiro注解导致aop失效的问题
     */
    private boolean proxyTargetClass;

    /**
     * 是否设置CaptchaFormAuthenticationFilter.java
     */
    private boolean onLoginSuccess;

    /**
     * 密码
     */
    public static class Password{

        private boolean enable;

        /**
         * 实现securityService中验证部分的类别名
         * todo 目前写的固定值
         */
        private String serviceName = "passwordSecurityService";

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

    /**
     * CookieRememberMeManager
     */
    public static class CookieRememberMeManager{

        private boolean enable;

        private String cookieName = "rememberMe";

        private boolean cookieHttpOnly = true;

        private Integer cookieMaxAge = 2592000;

        private String cipherKey = "2AvVhdsgUs0FSA3SDFAdag==";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }

        public boolean isCookieHttpOnly() {
            return cookieHttpOnly;
        }

        public void setCookieHttpOnly(boolean cookieHttpOnly) {
            this.cookieHttpOnly = cookieHttpOnly;
        }

        public Integer getCookieMaxAge() {
            return cookieMaxAge;
        }

        public void setCookieMaxAge(Integer cookieMaxAge) {
            this.cookieMaxAge = cookieMaxAge;
        }

        public String getCipherKey() {
            return cipherKey;
        }

        public void setCipherKey(String cipherKey) {
            this.cipherKey = cipherKey;
        }
    }

    /**
     * EhCacheManager
     */
    public class EhCacheManager{

        private boolean enable;

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

    public CookieRememberMeManager getCookieRememberMeManager() {
        return cookieRememberMeManager;
    }

    public void setCookieRememberMeManager(CookieRememberMeManager cookieRememberMeManager) {
        this.cookieRememberMeManager = cookieRememberMeManager;
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public boolean isOnLoginSuccess() {
        return onLoginSuccess;
    }

    public void setOnLoginSuccess(boolean onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public EhCacheManager getEhCacheManager() {
        return ehCacheManager;
    }

    public void setEhCacheManager(EhCacheManager ehCacheManager) {
        this.ehCacheManager = ehCacheManager;
    }
}
