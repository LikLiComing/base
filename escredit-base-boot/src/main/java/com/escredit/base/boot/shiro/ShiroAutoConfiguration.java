package com.escredit.base.boot.shiro;

import com.escredit.base.boot.shiro.filter.ApiFilter;
import com.escredit.base.boot.shiro.filter.CaptchaFormAuthenticationFilter;
import com.escredit.base.boot.shiro.jwt.JwtFilter;
import com.escredit.base.boot.shiro.jwt.JwtRealm;
import com.escredit.base.boot.shiro.realm.CodeRealm;
import com.escredit.base.boot.shiro.realm.PasswordRealm;
import com.escredit.base.boot.shiro.realm.UserModularRealmAuthenticator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
@ConditionalOnProperty(prefix = "escredit.base.boot.shiro", name = "enable", havingValue = "true")
public class ShiroAutoConfiguration {

    private ShiroProperties shiroProperties;

    public ShiroAutoConfiguration(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    @Autowired(required = false)
    private ApiFilter apiFilter;

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(1000 * 60 * 60 * 24);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        return defaultWebSessionManager;
    }

    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        factoryBean.setEternal(false);
        factoryBean.setTimeToIdleSeconds(120);
        factoryBean.setTimeToLiveSeconds(120);
        factoryBean.setDiskExpiryThreadIntervalSeconds(900);
        factoryBean.setCacheName("shiroCache");

        CacheManager cm = CacheManager.create();
        Cache cache = new Cache(factoryBean);
        cm.addCache(cache);
        EhCacheManager ecacheManager = new EhCacheManager();
        ecacheManager.setCacheManager(cm);
        return ecacheManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager 安全管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 密码登录，验证码时使用该匹配器进行匹配
     * @return HashedCredentialsMatcher
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME);
        matcher.setHashIterations(1);
        return matcher;
    }

    /**
     * 密码登录Realm
     * @return
     */
    @Bean
    public PasswordRealm passwordRealm(){
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return passwordRealm;
    }

    /**
     * 验证码登录Realm
     * @return CodeRealm
     */
    @Bean
    public CodeRealm codeRealm(){
        CodeRealm codeRealm = new CodeRealm();
        codeRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return codeRealm;
    }

    /**
     * jwtRealm
     * @return JwtRealm
     */
    @Bean
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }

    /**
     * Shiro内置过滤器，可以实现拦截器相关的拦截器
     *    常用的过滤器：
     *      anon：无需认证（登录）可以访问
     *      authc：必须认证才可以访问
     *      user：如果使用rememberMe的功能可以直接访问
     *      perms：该资源必须得到资源权限才可以访问
     *      role：该资源必须得到角色权限才可以访问
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        bean.setSecurityManager(securityManager);
        // 设置未登录跳转url
        bean.setLoginUrl(shiroProperties.getLoginUrl());
        bean.setSuccessUrl(shiroProperties.getSuccessUrl());

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //静态资源
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/assets/**","anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/fonts/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/plugin/**","anon");

        //swagger
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/webjars/**","anon");
        filterChainDefinitionMap.put("/csrf/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/doc.html","anon");

        shiroProperties.getFilterChainDefinitionList().stream().forEach(item->{
            String[] split = item.split(",");
            if(split.length==2){
                filterChainDefinitionMap.put(split[0],split[1]);
            }
        });
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        Map filter = new HashMap();
        if(shiroProperties.getJwt().isEnable()){
            String jwtKey = "jwt";
            filter.put(jwtKey, new JwtFilter(shiroProperties));
        }
        if(shiroProperties.getApi().isEnable()){
            String apiKey = "api";
            apiFilter.setShiroProperties(shiroProperties);
            filter.put(apiKey, apiFilter);
        }
        if(shiroProperties.isOnLoginSuccess()){
            filter.put("authc",new CaptchaFormAuthenticationFilter());
        }
        bean.setFilters(filter);
        return bean;
    }

    @Bean
    public UserModularRealmAuthenticator userModularRealmAuthenticator(){
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     *  SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     */
    @Bean
    public SecurityManager securityManager(@Autowired UserModularRealmAuthenticator userModularRealmAuthenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setAuthenticator(userModularRealmAuthenticator);
        List<Realm> realms = new ArrayList<>();
        //密码
        if(shiroProperties.getPassword().isEnable()){
            realms.add(passwordRealm());
        }
        //验证码
        if(shiroProperties.getCode().isEnable()){
            realms.add(codeRealm());
        }
        //Jwt
        if(shiroProperties.getJwt().isEnable()){
            realms.add(jwtRealm());

            //关闭shiro自带的session，详情见文档
            DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
            DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
            defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
            subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
            securityManager.setSubjectDAO(subjectDAO);
        }
        securityManager.setRealms(realms);

        //cookie
        if(shiroProperties.getCookieRememberMeManager().isEnable()){
            ShiroProperties.CookieRememberMeManager cookieRememberMeManager = shiroProperties.getCookieRememberMeManager();
            SimpleCookie cookie = new SimpleCookie(cookieRememberMeManager.getCookieName());
            cookie.setHttpOnly(cookieRememberMeManager.isCookieHttpOnly());
            cookie.setMaxAge(cookieRememberMeManager.getCookieMaxAge());

            CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
            rememberMeManager.setCookie(cookie);
            rememberMeManager.setCipherKey(Base64.decode(cookieRememberMeManager.getCipherKey()));
            securityManager.setRememberMeManager(rememberMeManager);
        }
        //ehCacheManager
        if(shiroProperties.getEhCacheManager().isEnable()){
            securityManager.setCacheManager(ehCacheManager());
            securityManager.setSessionManager(defaultWebSessionManager());
        }

        return securityManager;
    }

//    @Bean
//    public EhCacheManager ehCacheManager(){
//        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
//        factoryBean.setEternal(false);
//        factoryBean.setTimeToIdleSeconds(120);
//        factoryBean.setTimeToLiveSeconds(120);
//        factoryBean.setDiskExpiryThreadIntervalSeconds(900);
//        factoryBean.setMaxElementsInMemory(10000);
//        factoryBean.setOverflowToDisk(false);
//        factoryBean.setDiskPersistent(false);
//        factoryBean.setCacheName("shiroCache");
//
//        CacheManager cm = CacheManager.create();
//        Cache cache = new Cache(factoryBean);
//        cm.addCache(cache);
//
//        EhCacheManager ecacheManager = new EhCacheManager();
//        ecacheManager.setCacheManager(cm);
//        return ecacheManager;
//    }

    /**
     * 设置为cglib代理方式，解决shiro注解导致aop失效问题
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(shiroProperties.isProxyTargetClass());
        return creator;
    }

    /**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
}
