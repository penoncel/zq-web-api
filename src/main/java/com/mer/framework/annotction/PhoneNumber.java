package com.mer.framework.annotction;

import com.mer.framework.annotction.validator.PhoneNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @Program: zq-web-api
 * @Description: 自定义手机号注解 用来验证手机号格式
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "手机号无效";
    Class[] groups() default {};
    Class[] payload() default {};
}
