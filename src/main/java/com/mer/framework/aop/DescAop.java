package com.mer.framework.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mer.framework.annotction.DES;
import com.mer.common.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SecretAOPController
 * @description: 切面加密解密
 * @author: zq
 **/
@Aspect
@Component
@Slf4j
public class DescAop {
    

    /**
     * 是否进行加密解密，通过配置文件注入（不配置默认为true）
     */
    @Value("${isSecret:true}")
    boolean isSecret;

    /**
     * 定义切点,使用了@Secret注解的类 或 使用了@Secret注解的方法
     */
    @Pointcut("@within(com.mer.framework.annotction.DES) || @annotation(com.mer.framework.annotction.DES)")
    public void pointcut(){}

    /**
     * 环绕切面
     * @param point
     * @return
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point){
        try {
            // 接收到请求，记录请求内容
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 参数[%s] ,Arrays.toString(point.getArgs())
            log.info(String.format("{DESAOP}请求地址[%s] 请求类型[%s] 实现方法[%s] ",request.getRequestURL().toString(),request.getMethod(),(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName())    ));
            // 获取被代理对象
            Object target = point.getTarget();
            // 获取通知签名
            MethodSignature signature = (MethodSignature )point.getSignature();
            // 获取被代理方法
            Method pointMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            // 获取被代理方法上面的注解@Secret
            DES secret = pointMethod.getAnnotation(DES.class);
            // 被代理方法上没有，则说明@Secret注解在被代理类上
            if(secret==null){
                secret = target.getClass().getAnnotation(DES.class);
            }else{
                // 获取被代理方法参数
                Object[] args = point.getArgs();
                if (args == null || args.length == 0) {
                    return error(500001,"参数不能为空");
                }
                // 获取注解上声明的加密参数名
                String signName = secret.signName();
                for (Object obj : args){
                    if(obj instanceof Map){
                        Map<String, String> param = (Map<String, String>) obj;
                        if (param != null && param.get(signName) != null) {
                            log.info("{DESAOP}请求报文：["+param.toString()+"]");
                            log.info("{DESAOP}待解密参数：["+param.get(secret.signName())+"]");
                            try{
                                // 解密
                                String str = AesUtils.decrypt(param.get(secret.signName()));
                                log.info("{DESAOP}解密后参数：["+str+"]");
                                // 转换vo
                                obj = JSON.parseObject(str);
                                //移除掉原来的sing
                                param.remove(param.get(secret.signName()));
                                //设置新的sing
                                param.put(secret.signName(),obj.toString());
                                log.info("{DESAOP}方法前参数：["+param.toString()+"]");
                            }catch (Exception e){
                                return error(500002,"解密失败");
                            }
                        }else{
                            return error(500003,signName +"参数不能为空");
                        }
                    }
                }

                // 执行请求
                Object result =  point.proceed();
                // 判断配置是否需要返回加密
                if(isSecret){
                    Map<String,Object> respMap = JSON.parseObject(result.toString(),Map.class);
                    String data = respMap.get("data").toString();
                    if(data!="" && data!=null){
                        respMap.put("data", AesUtils.encrypt(data));
                    }
                    String msg = JSON.toJSONString(respMap);
                    log.info("{DESAOP}响应结果：["+msg+"]");
                    return msg;
                }
                return result;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return error(500004,"@DES 注解指定的类没有字段:encryptStr,或encryptStrName参数字段不存在");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 组织反回信息
     * @param code 响应码
     * @param msg  美容
     * @return
     */
    public static String error(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        msg = json.toJSONString();
        log.error("{DESAOP}：["+msg+"]");
        return msg;
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
