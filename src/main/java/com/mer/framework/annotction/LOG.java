package com.mer.framework.annotction;

import java.lang.annotation.*;

/**
 * @Program: zq-web-api
 * @Description: 日志注解 @LOG
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Target({ElementType.METHOD, ElementType.TYPE})//注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface LOG {
    String operModul() default ""; // 操作模块
    String operType() default "";  // 操作类型
    String operDesc() default "";  // 操作说明
}
