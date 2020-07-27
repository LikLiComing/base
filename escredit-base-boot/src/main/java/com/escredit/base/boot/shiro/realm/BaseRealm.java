package com.escredit.base.boot.shiro.realm;

import com.escredit.base.boot.shiro.service.SecurityService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by liyongping on 2020/7/13 1:19 PM
 */
public class BaseRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(BaseRealm.class);

    private SecurityService securityService;

    public SecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object principal = principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleNames = securityService.findRoles(principal);
        Set<String> permissions = securityService.findPermissions(principal);
        info.setRoles(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 获取身份认证信息
     * @param authenticationToken authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Object principal = authenticationToken.getPrincipal();
        Object credentials = authenticationToken.getCredentials();
        Object[] pc = securityService.varifyPrincipalAndCredentials(principal,credentials);
        if(pc.length>=2){
            principal = pc[0];
            credentials = pc[1];
        }
        //取salt
        if(pc.length==3){
            return new SimpleAuthenticationInfo(principal, credentials, ByteSource.Util.bytes(pc[2]),getName());
        }
        return new SimpleAuthenticationInfo(principal, credentials, getName());
    }
}
