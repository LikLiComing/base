package com.escredit.base.boot.aop.interceptor;


import com.escredit.base.boot.aop.annotation.Idempotent;
import com.escredit.base.boot.aop.idempotent.IdempotentService;
import com.escredit.base.exception.BusinessException;
import com.escredit.base.util.RequestUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 防止重复提交AOP拦截器
 */
public class IdempotentMethodInterceptor implements MethodInterceptor {
    /**
     * 防止接口重复提交header参数
     */
    private static final String AUTHENTICATION = "Authentication";

    @Autowired(required = false)
    private IdempotentService idempotentService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //获取幂等性注解对象
        Idempotent idempotent = invocation.getMethod().getAnnotation(Idempotent.class);
        //幂等性未启用
        if (!idempotent.enable()) {
            return invocation.proceed();
        }
        HttpServletRequest request = RequestUtils.getRequest();
        //客户端发送的防止接口重复提交header参数
        String authentication = request.getHeader(AUTHENTICATION);
        if (StringUtils.isEmpty(authentication)) {
            throw new BusinessException("-1", "幂等性验证Header(Authentication)不可为空！");
        }
        boolean delFlag = idempotentService.delete(authentication);
        if (!delFlag) {
            throw new BusinessException("-1","幂等性验证不通过");
        }
        return invocation.proceed();
    }
}
