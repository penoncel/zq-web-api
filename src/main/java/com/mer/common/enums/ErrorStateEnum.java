package com.mer.common.enums;

import com.mer.common.constant.Constant;

/**
 * @Program: zq-web-api
 * @Description: 接口异常信息 枚举类
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public enum ErrorStateEnum {
    // 系统错误状态码
    SUCCESS(200, "成功"),
    // 系统错误状态码
    GENERAL_EXCEPTION(10000, "通用异常"),
    // 发送短信失败
    SEND_SMS_ERROR(10001, "短信发送失败"),
    // 该用户已存在
    USER_ALREADY_EXIST(1001, "该用户已存在"),
    // 验证码无效
    CODE_EXPIRE(1002, "验证码无效"),
    // 验证码不正确
    CODE_ERROR(1003, "验证码不正确"),
    // 用户名不存在
    USERNAME_NOT_EXIST(1004, "用户名不存在"),
    // 密码不正确
    PASSWORD_ERROR(1005, "密码不正确"),
    // 没有相关权限
    NOT_AUTH(1006, "没有相关权限"),

    //token 格式错误
    TOKEN_ERROR(1007, Constant.TOKEN_HEADER_NAME+"  格式错误!"),
    // token 无效
    TOKEN_INVALID(1008, Constant.TOKEN_HEADER_NAME+" 已过期，请重新登录 !"),
    //token 为空
    TOKEN_ISNULL(1009, " 拒绝访问!"),
    //token 中手机号为空
    TOKEN_PHONE_IS_NULL(1011, Constant.TOKEN_HEADER_NAME+" 无效 ,"+Constant.TOKEN_HEADER_NAME+"解密后手机号为空 !"),
    //token 中手机号为空
    TOKEN_PHONE_POJO_IS_NULL(1011, Constant.TOKEN_HEADER_NAME+" 无效 ,"+Constant.TOKEN_HEADER_NAME+"解密后手机号用户信息不存在 !"),

    // 缺少相应参数
    MISSING_PARAMETER(2009, "参数绑定失败:缺少参数"),
    // 服务器内部错误
    SYSTEM_ERROR(500, "服务器内异常"),
    // 服务器内部错误
    REQUEST_LIMIT(9001, "访问过于频繁，请稍后再试。")
    ;


    private Integer code;
    private String msg;

    ErrorStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResultEnums{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
