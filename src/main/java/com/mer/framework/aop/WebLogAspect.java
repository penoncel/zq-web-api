package com.mer.framework.aop;//package com.mer.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * @Program: zq-web-api
 * @Description: 系统日志切面
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@SuppressWarnings("ALL")
@Aspect
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);


    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    /**
     * 切点
     * 配置需要添加切面通知的包路径
     */
    @Pointcut("execution(* com.mer.project.controller.*.*(..))")
    public void webLog() {}

    /**
     * 前置通知
     * @param joinPoint 切点
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {

        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("[URL : " + request.getRequestURL().toString()+"]");
        logger.info("[HTTP_METHOD : " + request.getMethod()+"]");
        logger.info("[IP : " + request.getRemoteAddr()+"]");
        logger.info("[CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+"]");
        logger.info("[ARGS : " + request.getParameterMap()+"]");
        logger.info("[ParameS : " + URLDecoder.decode(Arrays.toString(joinPoint.getArgs())),request.getCharacterEncoding() +"]");
    }

    @After("webLog()")
    public void doAfter() {
        // 处理完请求，返回内容
        logger.info("[耗时（毫秒） : " + (System.currentTimeMillis() - startTime.get())+"]\n");
        startTime.remove();
    }

}
