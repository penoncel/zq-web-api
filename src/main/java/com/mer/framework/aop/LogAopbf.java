package com.mer.framework.aop;

import com.mer.common.utils.ComUtils;
import com.mer.common.utils.DateUtils;
import com.mer.framework.annotction.LOG;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * 切面处理类，操作日志异常日志记录处理
 *
 * @author zq
 */
@Aspect
@Component
public class LogAopbf {
    private static final Logger logger = LoggerFactory.getLogger(LogAopbf.class);

    /**
     * 进入方法时间戳
     */
    private Long startTime;
    /**
     * 方法结束时间戳(计时)
     */
    private Long endTime;

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
    public void operExceptionLogPoinCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        setLogMsg(joinPoint, keys, null);
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        setLogMsg(joinPoint, null, e);
    }

    public void setLogMsg(JoinPoint joinPoint, Object keys, Throwable throwable) {
        String startTime = DateUtils.yyyyMMddHHmmssSss();
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //获取请求头中的User-Agent
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        LOG opLog = method.getAnnotation(LOG.class);

        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
//            operlog.setOperMethod(methodName); // 请求方法

        // 请求的参数
        Map<String, String> rtnMap = ComUtils.converMap(request.getParameterMap());
        // 将参数所在的数组转换成json
        logger.info("{LOGAop}操作模块          |->  " + opLog.operModul());
        logger.info("{LOGAop}操作类型          |->  " + opLog.operType());
        logger.info("{LOGAop}操作描述          |->  " + opLog.operDesc());
        logger.info("{LOGAop}硬件设备          |->  " + userAgent.getBrowser().toString());
        logger.info("{LOGAop}设备系统          |->  " + userAgent.getOperatingSystem().toString());
        logger.info("{LOGAop}设备版本          |->  " + userAgent.getBrowserVersion());
        logger.info("{LOGAop}请求URI           |->  " + request.getRequestURI());
        logger.info("{LOGAop}请求I P           |->  " + getIpAddr(request));
        logger.info("{LOGAop}操作员            |->  " + "赵旗");
        logger.info("{LOGAop}操作时间          |->  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logger.info("{LOGAop}操作方法          |->  " + methodName);
        logger.info("{LOGAop}请求参数          |->  " + rtnMap.toString());
        if (Objects.nonNull(keys) && Objects.isNull(throwable)) {
            //成功
            logger.info("{LOGAop}返回结果          |->  " + keys);
        }
        if (Objects.isNull(keys) && Objects.nonNull(throwable)) {
            //失败
            logger.info("{LOGAop}异常名称          |->  " + throwable.getClass().getName());
            logger.info("{LOGAop}异常信息          |->  " + throwable.getMessage());
        }
        logger.info("{LOGAop}请求结束时间      |->  " + LocalDateTime.now());
        String endTime = DateUtils.yyyyMMddHHmmssSss();
        logger.info("{LOGAop}请求耗时          |->  " + DateUtils.getMinSec(startTime, endTime));
        logger.info("{LOGAop}######################################END###########################################\r");

    }


    /**
     * [方法] getIpAddr
     * [描述] 得到真实的IP
     * [参数] HttpServletRequest
     * [返回] String
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}