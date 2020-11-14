package com.escredit.base.boot.shiro.filter;


import com.alibaba.fastjson.JSON;
import com.escredit.base.boot.shiro.ShiroProperties;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.codec.SignTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对外开放api filter
 * 需要开启shiro.api.enable = true
 */
public class ApiFilter extends BaseFilter {

    private Logger logger = LoggerFactory.getLogger(ApiFilter.class);

    private ShiroProperties shiroProperties;

    public ShiroProperties getShiroProperties() {
        return shiroProperties;
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    /**
     * 执行登录认证
     *
     * @param request     ServletRequest
     * @param response    ServletResponse
     * @param mappedValue mappedValue
     * @return 是否成功
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String targetUrl = httpServletRequest.getRequestURI();
        String[] whiteUrl = shiroProperties.getApi().getWhiteUrl();
        if(whiteUrl != null && whiteUrl.length >=1){
            if (Arrays.stream(shiroProperties.getApi().getWhiteUrl()).anyMatch(url -> targetUrl.contains(url))) {
                return true;
            }
        }

        DTO dto = checkSign(request);
        if (dto.isSuccess()) {
            return true;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSON(dto).toString());
            writer.flush();
        } catch (IOException e1) {
            logger.error(e1.getMessage());
        }
        return false;
    }

    private DTO checkSign(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        DTO dto = new DTO();
        Map<String, String[]> paramMap = request.getParameterMap();
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account) || !account.equals(shiroProperties.getApi().getAccount())) {
            return dto.putErr("1001", "账号不存在或被禁用");
        }

        //排序后的入参
        Map paramMapSort = new LinkedHashMap();
        paramMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> {
            //sign，json串，非简单数据类型都不纳入加密
            if ("sign".equals(e.getKey()) || e.getValue().length > 1
                    || e.getKey().indexOf("Json") != -1 || e.getKey().indexOf("List") != -1 || e.getKey().indexOf("Array") != -1) {
                return;
            }

            if (e.getValue() != null && e.getValue().length > 0) {
                paramMapSort.put(e.getKey(), e.getValue()[0]);
            }
        });

        String signExpected = SignTools.getSignature(paramMapSort, shiroProperties.getApi().getKey());
        //校验数字签名
        String sign = request.getParameter(shiroProperties.getApi().getSignName());
        if (StringUtils.isEmpty(sign) || !sign.equals(signExpected)) {
            return dto.putErr("1002", "数据签名失败");
        }
        return afterHandle(request,dto);
    }

    /**
     * 后置处理
     * @param request
     * @param dto
     * @return
     */
    public DTO afterHandle(HttpServletRequest request,DTO dto){
        return dto;
    }

}
