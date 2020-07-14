package com.escredit.base.boot.shiro.service;

import com.escredit.base.boot.shiro.ShiroProperties;
import com.escredit.base.boot.shiro.jwt.JwtUtil;
import com.escredit.base.entity.DTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liyongping on 2020/7/13 12:53 PM
 */
public abstract class SecurityService {


    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * 验证并获取封装后的Principal Credentials
     * @return obj[0]=Principal,obj[1]=Credentials
     */
    public abstract Object[] varifyPrincipalAndCredentials(Object principal,Object credentials);

    /**
     * 获取拥有角色
     * @param principal
     * @return
     */
    public Set<String> findRoles(Object principal){
        return new HashSet();
    }

    /**
     * 获取拥有权限
     * @param principal
     * @return
     */
    public Set<String> findPermissions(Object principal){
        return new HashSet();
    }

    public DTO login(AuthenticationToken token){
        DTO dto = new DTO();
        Subject userSub = SecurityUtils.getSubject();
        String error ="";
        try {
            userSub.login(token);
            Object user = userSub.getPrincipal();
            dto.setObject(user);
        }catch (IncorrectCredentialsException e) {
            error = "验证码错误.";
        } catch (ExcessiveAttemptsException e) {
            error = "登录失败次数过多";
        } catch (LockedAccountException e) {
            error = "帐号已被锁定.";
        } catch (DisabledAccountException e) {
            error = "帐号已被禁用.";
        } catch (ExpiredCredentialsException e) {
            error = "帐号已过期.";
        } catch (UnknownAccountException e) {
            error = "手机号不存在";
        } catch (UnauthorizedException e) {
            error = "您没有得到相应的授权！";
        }catch (Exception e) {
            error = "请重新登录！";
        }
        dto.setSuccess(StringUtils.isEmpty(error)?true:false);
        if(!dto.isSuccess()){
            dto.putErr("-1",error);
        }
        return dto;
    }

    /**
     * 帐号密码登录
     * @param account
     * @param password
     * @param rememberMe
     * @return
     */
    public DTO loginByUsernamePassword(String account,String password,Boolean rememberMe){
        UsernamePasswordToken token = new UsernamePasswordToken(account,password,Boolean.valueOf(rememberMe));
        DTO dto = this.login(token);
        return dto;
    }

    /**
     * 获取最新token
     * @param obj
     * @param secret
     * @return
     */
    public String refreshToken(Object obj,String secret){
        String jwtToken = JwtUtil.sign(obj,secret,shiroProperties.getJwt().getExpireTime());
        return jwtToken;
    }

}
