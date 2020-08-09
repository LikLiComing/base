package com.escredit.base.boot.aop.log;

import com.escredit.base.util.RequestUtils;
import com.escredit.base.util.codec.HiddenFieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Aspect
@EnableConfigurationProperties(LogAopProperties.class)
@ConditionalOnProperty(prefix = "escredit.base.boot.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger("WebLogAspect");

    /**
     * 消费方实现
     */
    @Autowired(required = false)
    private LogService logService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void webLog() {}

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    private static final String PRE_LOG = "preLog";

    @Around(value = "webLog()")
    public Object weblog(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = null;
        // 开始时间。
        long startTime = System.currentTimeMillis();
        // 请求参数。
        StringBuilder requestStr = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if(arg!=null) {
                    requestStr.append(arg.toString());
                }
            }
        }

        //请求ip
        String ip="";
        String url = "";
        try {
            HttpServletRequest request = RequestUtils.getRequest();
            ip = RequestUtils.getIp(request);
            url = request.getRequestURI();
        } catch (Exception e) {
        }

        //方法
        String clazzName = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        //用户
        String account="vistor";
        if(logService != null){
            account = logService.getCurrentUser();
            try {
                account = HiddenFieldUtils.hiddenPhoneNum(account);
            }catch (Exception e){
                logger.debug("account:{}",e.getMessage());
            }
        }

        Map<String, Object> threadInfo = new HashMap<>();
        String preLog = String.format("IP:%s User:%s Method:%s Request:%s Url:%s"
                ,ip,account,clazzName+methodName, requestStr,url);
        threadInfo.put(PRE_LOG, preLog);

        obj = joinPoint.proceed(args);
        long duration = System.currentTimeMillis() - startTime;

        logger.info("{} Duration:{} Result:{}",preLog,duration,obj);
        return obj;
    }

    @AfterThrowing(value = "webLog()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        Map<String, Object> threadInfo = threadLocal.get();
        if(threadInfo != null){
            logger.error("{} Url:{} Duration:{} Result:{}",threadInfo.get(PRE_LOG),0,throwable.getMessage());
        }
    }

}
