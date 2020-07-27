package com.escredit.base.boot.shiro.realm;


import com.escredit.base.boot.shiro.jwt.JwtRealm;
import com.escredit.base.boot.shiro.jwt.JwtToken;
import com.escredit.base.boot.shiro.token.CodeToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();

        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        String realmName;
            //单独处理jwt
            if(authenticationToken instanceof JwtToken){
                realmName = JwtRealm.realmName;
            }else if(authenticationToken instanceof CodeToken){
                realmName = CodeRealm.realmName;
            }else{
                realmName = PasswordRealm.realmName;
            }
        for (Realm realm : realms) {
            if (realm.getName().contains(realmName)){
                typeRealms.add(realm);
            }
        }
        // 判断是单Realm还是多Realm
        if(typeRealms.size() == 1){
            return doSingleRealmAuthentication(typeRealms.iterator().next(), authenticationToken);
        }else {
            return doMultiRealmAuthentication(typeRealms, authenticationToken);
        }

    }
}
