package com.escredit.base.boot.shiro.service;

import com.escredit.base.boot.shiro.ShiroProperties;
import com.escredit.base.boot.shiro.jwt.JwtToken;
import com.escredit.base.boot.shiro.jwt.JwtUtil;
import com.escredit.base.boot.shiro.token.CodeToken;
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
     * @return obj[0]=Principal,obj[1]=Credentials,obj[2]=salt
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
        boolean isTel = (token instanceof JwtToken || token instanceof CodeToken);
        String credentialsName = isTel?"验证码":"密码";
        String accountName = isTel?"手机号":"帐号";
        try {
            userSub.login(token);
            Object user = userSub.getPrincipal();
            dto.setObject(user);
        } catch (IncorrectCredentialsException e) {
            error = credentialsName+"错误";
        } catch (ExpiredCredentialsException e) {
            error = credentialsName+"已过期";
        } catch (LockedAccountException e) {
            error = accountName+"已被锁定";
        } catch (DisabledAccountException e) {
            error = accountName+"已被禁用";
        }  catch (UnknownAccountException e) {
            error = accountName+"不存在";
        } catch (UnauthorizedException e) {
            error = "您没有得到相应的授权！";
        } catch (ExcessiveAttemptsException e) {
            error = "登录失败次数过多";
        } catch (Exception e) {
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
     * @param tel
     * @param code
     * @param rememberMe
     * @return
     */
    public DTO loginByUsernamePassword(String tel,String code,Boolean rememberMe){
        UsernamePasswordToken token = new UsernamePasswordToken(tel,code,Boolean.valueOf(rememberMe));
        DTO dto = this.login(token);
        return dto;
    }

    /**
     * 手机验证码登录
     * @param account
     * @param code
     * @param rememberMe
     * @return
     */
    public DTO loginByCode(String account,String code,Boolean rememberMe){
        CodeToken token = new CodeToken(account,code,Boolean.valueOf(rememberMe));
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
