package com.mer.common.rediskey;

import com.mer.common.constant.Constant;
import com.mer.framework.config.redis.keyset.BasePrefix;

/**
 * @Program: zq-web-api
 * @Description: 用户登入 key
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class AppLoginKey extends BasePrefix {

    private AppLoginKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    /**
     * 用户信息 token
     */
    public static AppLoginKey userToken = new AppLoginKey(Constant.REDIS_TOKEN_TIMEOUT,Constant.TOKEN_HEADER_NAME+":");

    /**
     * 登入用户 信息
     */
    public static AppLoginKey loginUserIinfo = new AppLoginKey(Constant.LOGINUSERMSG_TIMEOUT,"userInfo:");

    /**
     * 登入 验证码 key
     */
    public static AppLoginKey smsCode = new AppLoginKey(Constant.SMSCODE_TIMEOUT,"smsCode:");


    /**
     * 通过验证码 修改密码
     */
    public static AppLoginKey modifyPassWordCode = new AppLoginKey(Constant.SMSCODE_TIMEOUT,"modifyPassWordCode:");

}
