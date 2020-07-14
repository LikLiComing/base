package com.escredit.base.boot.shiro.jwt;

import com.alibaba.fastjson.JSON;
import com.escredit.base.boot.shiro.ShiroProperties;
import com.escredit.base.boot.shiro.filter.BaseFilter;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.lang.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 需要开启shiro.filter.enable = true
 */
public class JwtFilter extends BaseFilter {

    private Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private ShiroProperties shiroProperties;

    public JwtFilter(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    /**
     * 执行登录认证
     * @param request ServletRequest
     * @param response ServletResponse
     * @param mappedValue mappedValue
     * @return 是否成功
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = ((HttpServletRequest) request).getHeader(shiroProperties.getJwt().getTokenName());
        //图片路径，会传token
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(shiroProperties.getJwt().getTokenName());
        }
        if (token != null) {
            try {
                executeLogin(request, response);
                return true;
            } catch (IncorrectCredentialsException e) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                try (PrintWriter writer = response.getWriter()) {
                    DTO dto = new DTO(false);
                    dto.putErr("-1","请重新登录");
                    writer.write(JSON.toJSON(dto).toString());
                    writer.flush();
                    return false;
                } catch (IOException e1) {
                    logger.error(e1.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 执行登录
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(shiroProperties.getJwt().getTokenName());
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(shiroProperties.getJwt().getTokenName());
        }
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

}
