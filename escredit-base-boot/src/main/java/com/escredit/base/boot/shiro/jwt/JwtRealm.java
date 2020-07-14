package com.escredit.base.boot.shiro.jwt;

import com.escredit.base.boot.shiro.realm.BaseRealm;
import com.escredit.base.boot.shiro.service.SecurityService;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JwtRealm extends BaseRealm {

    @Override
    @Autowired(required = false)
    @Qualifier(value = "jwtSecurityService")
    public void setSecurityService(SecurityService securityService) {
        super.setSecurityService(securityService);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    public String getName() {
        return "Jwt";
    }

}

