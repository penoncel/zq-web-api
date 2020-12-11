package com.mer.framework.annotction.validator;

import com.mer.framework.annotction.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Program: zq-web-api
 * @Description: 手机号验证
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            // can be null
            return false;
        }
        return phoneField.matches("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$") && phoneField.length() > 8 && phoneField.length() < 14;
    }
}
