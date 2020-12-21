package com.mer.common.enums;

import com.mer.common.constant.Constant;
import lombok.Getter;

/**
 * @Description: 系统响应信息
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Getter
public enum SysMsgEnum {


    /**
     * 登入信息
     **/
    PASSWORD_ERROR(1005, "密码不正确"),
    USERNAME_NOT_EXIST(1004, "用户名不存在"),

    /**
     * 验证码信息
     **/
    CODE_ERROR(1003, "验证码不正确"),
    CODE_EXPIRE(1002, "验证码无效"),
    SEND_SMS_ERROR(1001, "短信发送失败"),

    /**
     * 系统基本信息
     **/
    THE_ONLY_ACCESS(600, "您的账号已在别地登入,如非本人,请及时修改密码！"),
    INTERNAL_SERVER_ERROR(500, "服务器开小差,请稍后重试"),
    MISSING_PARAMETER(432, "缺少关键参数,请检查"),
    TOKEN_INVALID(418, Constant.TOKEN_HEADER_NAME + "已过期,请重新登录"),
    TOKEN_ERROR(430, Constant.TOKEN_HEADER_NAME + "格式错误"),
    TOO_MANY_REQUEST(429, "访问过于频繁，请稍后再试"),
    NOT_AUTH(403, "拒绝访问"),
    ERROR(300, "失败"),
    SUCCESS(200, "成功"),
    ;


    private Integer code;
    private String msg;

    SysMsgEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
