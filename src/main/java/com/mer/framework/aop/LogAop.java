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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;


/**
 * 切面处理类，操作日志异常日志记录处理
 *
 * @author zq
 */
@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class LogAop {

    /**
     * 定义切点,使用了 @LOG 注解的类 或 使用了 @Secret 注解的方法
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@within(com.mer.framework.annotction.LOG) || @annotation(com.mer.framework.annotction.LOG)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object successLog(ProceedingJoinPoint point) throws Throwable {
        //开始时间
        String startTime = DateUtils.yyyyMMddHHmmssSss();
        // 接收到请求，记录请求内容
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的方法名 类名+方法名
        String className = point.getTarget().getClass().getName();
        // 请求的参数
        Map<String, String> rtnMap = ComUtils.converMap(request.getParameterMap());
        rtnMap.remove("password");
        rtnMap.remove("oldPassword");
        rtnMap.remove("repassword");
        String req_param = JSONUtils.toJSONString(rtnMap);
        // 获取操作注解
        LOG opLog = method.getAnnotation(LOG.class);

        log.info("StTime ==> " + startTime);
        log.info("IP     ==> " + ComUtils.getIpAddr(request));
        log.info("Type   ==> " + request.getMethod());
        log.info("Class  ==> " + className);
        log.info("Method ==> " + request.getRequestURI());
        log.info("Modul  ==> " + opLog.operModul());
        log.info("Type   ==> " + opLog.operType());
        log.info("Desc   ==> " + opLog.operDesc());
        log.info("Req    ==> " + req_param);
        // 执行请求
        Object result = point.proceed();
        String endTime = DateUtils.yyyyMMddHHmmssSss();
        String resultStr = "";
        boolean flag = false;
        if (result != null) {
            try {
                resultStr = JSONObject.toJSONString(result);
            } catch (Exception e) {
                flag = true;
            }
        }
        resultStr = (result == null ? "NULL" : (flag ? result.toString() : resultStr));
        log.info("Resp   ==> " + resultStr);
        log.info("EnTime ==> " + endTime);
        String sec = DateUtils.getMinSec(startTime, endTime);
        log.info("Sec    ==> " + sec);
        log.info("\r");

        Subject subject = SecurityUtils.getSubject();
        // 当前用户 认证成功的
        String username = subject.getPrincipal().toString();
        // 获取请求头中的 User-Agent
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //行为记录对象
        LogPojo logPojo = new LogPojo();
        //操作员
        logPojo.setUserName(subject.getPrincipal().toString());
        //硬件设备
        logPojo.setDevice(userAgent.getBrowser().toString() == null ? "" : userAgent.getBrowser().toString());
        //设备系统
        logPojo.setDeviceSys(userAgent.getOperatingSystem().toString() == null ? "" : userAgent.getOperatingSystem().toString());
        //设备版本
        if (Objects.isNull(userAgent.getBrowserVersion())) {
            logPojo.setDeviceV(userAgent.getBrowser().toString());
        } else {
            logPojo.setDeviceV(userAgent.getBrowserVersion().toString());
        }
        //操作模块
        logPojo.setOperModule(opLog.operModul());
        //操作类型
        logPojo.setOperType(opLog.operType());
        //操作描述
        logPojo.setOperMsg(opLog.operDesc());
        //请求URI
        logPojo.setReqUri(request.getRequestURI());
        //请求I P
        logPojo.setReqIp(ComUtils.getIpAddr(request));
        logPojo.setOperMethod(className);//操作方法
        logPojo.setOperTimes(startTime);//操作时间
        logPojo.setReqParameter(req_param);//请求参数
        // 耗时
        logPojo.setTakeUpTime(sec);
        // 返回结果
        logPojo.setRespParameter(resultStr);
        logPojo.setRespTimes(endTime);
        logPojo.setLogType(request.getRequestURI().contains("/app/login") ? 1 : 2);
        logPojo.setLogStatus(1);
        return result;
    }

}