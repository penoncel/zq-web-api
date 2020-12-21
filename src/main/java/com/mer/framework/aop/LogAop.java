package com.mer.framework.aop;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.mer.common.utils.ComUtils;
import com.mer.common.utils.DateUtils;
import com.mer.framework.annotction.LOG;
import com.mer.framework.aop.pojo.LogPojo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * 切面处理类，操作日志异常日志记录处理
 *
 * @author zq
 */
@Aspect
@Component
@Slf4j
public class LogAop {

    /**
     * 定义切点,使用了 @OperLog 注解的类 或 使用了@Secret注解的方法
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@within(com.mer.framework.annotction.LOG) || @annotation(com.mer.framework.annotction.LOG)")
    public void operLogPoinCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.mer.project.controller..*.*(..))")
    public static void operExceptionLogPoinCut() {
    }

    public void setMsg(JoinPoint joinPoint, Object keys, Throwable throwable) {
        try {
            HttpServletRequest request = (HttpServletRequest) RequestContextHolder.getRequestAttributes().resolveReference(RequestAttributes.REFERENCE_REQUEST);
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            //获取请求头中的 User-Agent
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            // 认证信息
            Subject subject = SecurityUtils.getSubject();
            // 日志信息
            LogPojo logPojo = new LogPojo();
            if (subject.getPrincipal() != null) {
                // 操作员
                logPojo.setUserName(subject.getPrincipal().toString());
            }
            // 硬件设备
            logPojo.setDevice(userAgent.getBrowser() + "");
            // 设备系统
            logPojo.setDeviceSys(userAgent.getOperatingSystem() + "");
            // 设备版本
            logPojo.setDeviceV(userAgent.getBrowserVersion() + "");
            // 获取日志信息
            LOG opLog = method.getAnnotation(LOG.class);
            if (opLog != null) {
                // 操作模块
                logPojo.setOperModule(opLog.operModul());
                // 操作类型
                logPojo.setOperType(opLog.operType());
                // 操作描述
                logPojo.setOperMsg(opLog.operDesc());
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            // 请求的参数
            Map<String, String> rtnMap = ComUtils.converMap(request.getParameterMap());
            // 请求uri
            logPojo.setReqUri(request.getRequestURI());
            // 请求ip
            logPojo.setReqIp(ComUtils.getIpAddr(request));
            // 操作方法
            logPojo.setOperMethod(className + "." + methodName);
            // 操作时间
            logPojo.setOperTimes(DateUtils.yyyyMMddHHmmssSss());
            // 请求参数
            logPojo.setReqParameter(JSONUtils.toJSONString(rtnMap));
            // 日志类型
            logPojo.setLogType(logPojo.getReqUri().contains("/app/login") ? 1 : 2);
            // 响应信息
            logPojo.setRespParameter(keys != null ? JSONObject.toJSON(keys) + "" : throwable.getMessage());
            // 日志状态
            logPojo.setLogStatus(keys != null ? 1 : 2);

            log.info("Times     ==> " + logPojo.getOperTimes());
            log.info("Device    ==> " + logPojo.getDevice());
            log.info("DeviceSys ==> " + logPojo.getDeviceSys());
            log.info("DeviceV   ==> " + logPojo.getDeviceV());
            log.info("OpModule  ==> " + logPojo.getOperModule());
            log.info("OpType    ==> " + logPojo.getOperType());
            log.info("OpMsg     ==> " + logPojo.getOperMsg());
            log.info("Ip        ==> " + logPojo.getReqIp());
            log.info("Uri       ==> " + logPojo.getReqUri());
            log.info("User      ==> " + logPojo.getUserName());
            log.info("Method    ==> " + logPojo.getOperMethod());
            log.info("ReqParam  ==> " + logPojo.getReqParameter());
            log.info("RespParam ==> " + logPojo.getRespParameter() + "\r\n");
        } catch (Exception e) {
            ComUtils.writerEx(e);
            log.error("Aop异常 ==>" + e.getMessage());
        }
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        setMsg(joinPoint, keys, null);
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        setMsg(joinPoint, null, e);

    }


}