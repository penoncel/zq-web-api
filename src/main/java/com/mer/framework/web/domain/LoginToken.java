package com.mer.framework.web.domain;

import lombok.Data;

/**
 * @Program: zq-web-api
 * @Description: 反回登入的token vo
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Data
public class LoginToken {

    /**
     * token 用户正常访问接口时提交的token，过期时间设置长一些，15 day 吧
     */
    private String token;

    /**
     * refreshToken 刷新token 过期时间可以设置为token的两倍，甚至更长，用于动态刷新token时候提交后台验证
     */
    private String refreshToken;


    /**
     * token 过期时间戳，前端每次调用接口前需要主动判断是否已经过期，如果过期则提交 refreshToken 访问 token 刷新的接口进行刷新 token 获取心的 token 信息,然后存放本地，在次使用心得 token 进行业务访问。
     */
    private String tokenPeriodTime;


    /**
     * 动态刷新token
     * 前端检测到token过期后，携带refreshJwt访问后台刷新token的接口，服务端在拦截器中依然对refreshJwt进行解析鉴权
     *
     * 假如 refreshJwt 也过期了，提示登录过期，强制跳转登录页
     * 假如 refreshJwt 还在有效期，则签发新的token返回，前端使用最新的token进行接口请求
     */
}
