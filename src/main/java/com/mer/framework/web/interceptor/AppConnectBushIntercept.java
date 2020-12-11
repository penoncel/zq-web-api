package com.mer.framework.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.mer.common.enums.ErrorStateEnum;
import com.mer.common.rediskey.AppConnectKey;
import com.mer.common.utils.ServletUtils;
import com.mer.framework.annotction.RequestLimit;
import com.mer.framework.config.redis.redisservice.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Program: zq-web-api
 * @Description: 接口防刷 限制
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Slf4j
@Component
public class AppConnectBushIntercept extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AppConnectBushIntercept"+request.getRequestURI());
        /**
         * isAssignableFrom() 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
         * isAssignableFrom()方法是判断是否为某个类的父类
         * instanceof关键字是判断是否某个类的子类
         */
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            //HandlerMethod 封装方法定义相关的信息,如类,方法,参数等
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 获取方法中是否包含注解
            RequestLimit methodAnnotation = method.getAnnotation(RequestLimit.class);
            //获取 类中是否包含注解，也就是controller 是否有注解
            RequestLimit classAnnotation = method.getDeclaringClass().getAnnotation(RequestLimit.class);
            // 如果 方法上有注解就优先选择方法上的参数，否则类上的参数
            RequestLimit requestLimit = methodAnnotation != null?methodAnnotation:classAnnotation;
            if(requestLimit != null){
                if(isLimit(request,requestLimit)){
                    JSONObject o = new JSONObject();
                    o.put("code", ErrorStateEnum.REQUEST_LIMIT.getCode());
                    o.put("msg", ErrorStateEnum.REQUEST_LIMIT.getMsg());
                    ServletUtils.renderString(response, o.toString());
                    return false;
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 判断请求是否受限
     * @param request
     * @param requestLimit
     * @return
     */
    public boolean isLimit(HttpServletRequest request,RequestLimit requestLimit){
        // 受限的redis 缓存key ,因为这里用浏览器做测试，我就用sessionid 来做唯一key,如果是app ,可以使用 用户ID 之类的唯一标识。
        String limitKey = request.getServletPath()+request.getSession().getId();
        // 从缓存中获取，当前这个请求访问了几次
        String limiCount = redisService.getString(AppConnectKey.requestLimit,limitKey);
        Integer redisCount = limiCount==null?null:Integer.parseInt(limiCount);
        if(redisCount == null){
            // 初始 次数 1次
            redisService.set(AppConnectKey.requestLimit, limitKey, 1);
        }else{
            if(redisCount.intValue() >= requestLimit.maxCount()){
                return true;
            }
            // 次数 自增
            this.redisService.incr(AppConnectKey.requestLimit, limitKey);
        }

        return false;
    }

}