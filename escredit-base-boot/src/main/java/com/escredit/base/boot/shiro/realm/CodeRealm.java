package com.escredit.base.boot.shiro.realm;


import com.escredit.base.boot.shiro.service.SecurityService;
import com.escredit.base.boot.shiro.token.CodeToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 验证码登录
 */
@Component
public class CodeRealm extends BaseRealm{

    public static final String realmName = "Code";

    @Override
    @Autowired(required = false)
    @Qualifier(value = "codeSecurityService")
    public void setSecurityService(SecurityService securityService) {
        super.setSecurityService(securityService);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CodeToken;
    }

    @Override
    public String getName() {
        return realmName;
    }

}
