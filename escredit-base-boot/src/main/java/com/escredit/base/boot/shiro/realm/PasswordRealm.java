package com.escredit.base.boot.shiro.realm;


import com.escredit.base.boot.shiro.jwt.JwtToken;
import com.escredit.base.boot.shiro.service.SecurityService;
import com.escredit.base.boot.shiro.token.CodeToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 密码登录
 */
@Component
public class PasswordRealm extends BaseRealm{

    public static final String realmName = "Password";

    @Override
    @Autowired(required = false)
    @Qualifier(value = "passwordSecurityService")
    public void setSecurityService(SecurityService securityService) {
        super.setSecurityService(securityService);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return !(token instanceof JwtToken) && !(token instanceof CodeToken);
    }

    @Override
    public String getName() {
        return realmName;
    }

}
