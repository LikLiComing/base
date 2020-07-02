package com.escredit.base.boot.aop.idempotent;

import com.escredit.base.boot.aop.interceptor.IdempotentMethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(com.escredit.base.boot.aop.idempotent.IdempotentProperties.class)
@ConditionalOnProperty(prefix = "escredit.base.boot.idempotent", name = "enable", havingValue = "true")
public class IdempotentAutoConfiguration {

    private static final String REPEAT_SUBMIT_POINT_CUT = StringUtils.join("@annotation(com.escredit.base.boot.aop.annotation.Idempotent)");

    /**
     * 控制器AOP拦截处理
     */
    @Bean
    public DefaultPointcutAdvisor repeatSubmitPointCutAdvice() {
        //声明一个AspectJ切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        //设置切点表达式
        pointcut.setExpression(REPEAT_SUBMIT_POINT_CUT);
        // 配置增强类advisor, 切面=切点+增强
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        //设置切点
        advisor.setPointcut(pointcut);
        //设置增强（Advice）
        advisor.setAdvice(new IdempotentMethodInterceptor());
        //设置增强拦截器执行顺序
        advisor.setOrder(2);

        return advisor;
    }
}
