package com.escredit.base.boot.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CodeToken extends UsernamePasswordToken {

    public CodeToken(String tel,String code,final boolean rememberMe){
        super(tel,code,rememberMe);
    }

}
