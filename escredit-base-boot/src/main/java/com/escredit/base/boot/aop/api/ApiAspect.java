package com.escredit.base.boot.aop.api;

import com.alibaba.fastjson.JSON;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.RequestUtils;
import com.escredit.base.util.reflect.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Configuration
@Aspect
@EnableConfigurationProperties(ApiProperties.class)
public class ApiAspect {

    private Logger logger = LoggerFactory.getLogger(ApiAspect.class);

    @Autowired
    private ApiProperties apiProperties;

    @Pointcut("@annotation(com.escredit.base.boot.aop.api.Api)")
    public void api() {}

    @Around(value = "api()")
    public Object checkApi(ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        Api api = targetMethod.getAnnotation(Api.class);
        DTO dto = checkApi(api);
        if(!dto.isSuccess()){
            setErr(dto);
            if(String.class == targetMethod.getReturnType()){
                return "/error/500";
            }
        }
        Object obj = null;
        try {
            obj = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable throwable) {
        }
        return obj;
    }

    private DTO checkApi(Api api){
        DTO dto = new DTO(true);
        try {
            if(api == null){
                return dto;
            }
            Class obj = api.apiServiceImpl();
            if(obj == null){
                return dto;
            }
            ApiService apiService = (ApiService) obj.newInstance();
            dto = checkIdempotent(api, apiService);
            if(!dto.isSuccess()){
                return dto;
            }
            dto = checkPermission(api, apiService);
            if(!dto.isSuccess()){
                return dto;
            }
            dto = checkLimit(api, apiService);
            if(!dto.isSuccess()){
                return dto;
            }
        } catch (Exception e) {
        }
        return dto;
    }

    /**
     * 限流
     * @param api
     * @param apiService
     * @return
     */
    private DTO checkLimit(Api api, ApiService apiService) throws IOException {
        DTO dto = new DTO(true);
        if (!api.limit()) {
            return dto;
        }
        HttpServletRequest request = RequestUtils.getRequest();
        ApiProperties.Limit limit = apiProperties.getLimit();
        String key = RequestUtils.getIp(request) + request.getRequestURI();
        logger.info("限流日志："+key);
        dto = apiService.limit(key, limit);
        return dto;
    }

    /**
     * 校验幂等性
     * @param api
     * @param apiService
     */
    private DTO checkIdempotent(Api api, ApiService apiService) {
        DTO dto = new DTO(true);
        if (!api.idempotent()) {
            return dto;
        }
        HttpServletRequest request = RequestUtils.getRequest();
        //客户端发送的防止接口重复提交header参数
        String authentication = request.getHeader(apiProperties.getIdempotent().getTokenName());
        if (StringUtils.isEmpty(authentication)) {
            return dto.setSuccess(false).putErr("-1", "幂等性验证Token不可为空！");
        }
        boolean delFlag = apiService.delete(authentication);
        if (!delFlag) {
            return dto.setSuccess(false).putErr("-1", "幂等性验证不通过！");
        }
        return dto;
    }

    /**
     * 数据校验
     * @param api
     * @param apiService
     */
    private DTO checkPermission(Api api, ApiService apiService){
        DTO dto = new DTO(true);
        if(!api.permission()){
            return dto;
        }
        String[] methods = api.permissionMethods();
        for(String method: methods){
            dto = ReflectUtils.invokeMethod(apiService,method,null,null);
            if(!dto.isSuccess()){
                return dto;
            }
        }
        return dto;
    }

    /**
     * 设置错误反馈
     * @param dto
     */
    private void setErr(DTO dto){
        try {
            HttpServletRequest request = RequestUtils.getRequest();
            HttpServletResponse response = RequestUtils.getResponse();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            try (PrintWriter writer = response.getWriter()) {
                writer.write(JSON.toJSON(dto).toString());
                writer.flush();
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
            if(request!=null){
                request.setAttribute("errmsg",dto.getErrmsg());
            }
        } catch (Exception e) {
        }
    }

}
