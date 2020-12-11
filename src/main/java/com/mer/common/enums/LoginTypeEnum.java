package com.mer.common.enums;

/**
 * @Program: zq-web-api
 * @Description: 接口登入类型 枚举类
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public enum LoginTypeEnum {
    /**
     * 密码登录
     */
    PASSWORD_LOGIN_TYPE("PassWord"),
    /**
     * 验证码登录
     */
    CODE_LOGIN_TYPE("Code");

    private String type;

    LoginTypeEnum(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

}
