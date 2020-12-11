package com.mer.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

/**
 * @Program: zq-web-api
 * @Description: 公共工具
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class CommonsUtils {

    /**
     * 获取六位数验证码
     * @return 验证码
     */
    public static int getCode(){
        return (int)((Math.random()*9+1)*1000);
    }


    /**
     * 使用SHA256加密
     * @param password 需要加密的密码
     * @return 返回加密后的密码
     */
    public static String encryptPassword(String password){
        return String.valueOf(new SimpleHash("md5", password, null, 2));
    }


    /**
     * 获取uuid
     * @return string
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }


}
