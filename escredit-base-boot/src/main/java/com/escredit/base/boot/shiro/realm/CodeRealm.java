package com.escredit.base.boot.shiro.realm;


import com.escredit.base.boot.shiro.jwt.JwtToken;
import com.escredit.base.boot.shiro.service.SecurityService;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CodeRealm extends BaseRealm{

    @Override
    @Autowired(required = false)
    @Qualifier(value = "codeSecurityService")
    public void setSecurityService(SecurityService securityService) {
        super.setSecurityService(securityService);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return !(token instanceof JwtToken);
    }

    @Override
    public String getName() {
        return "Code";
    }

}
