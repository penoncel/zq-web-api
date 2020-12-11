package com.mer.framework.annotction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Program: zq-web-api
 * @Description: 加密注解 @DES 自定义注解，用来标识请求类 或者方法是否使用AOP加密解密
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Target({ElementType.TYPE,ElementType.METHOD})              // 可以作用在类上和方法上
@Retention(RetentionPolicy.RUNTIME)                               // 运行时起作用
public @interface DES {
    // 参数类中传递加密数据的属性名，默认 sign
    String signName() default "sign";
}
