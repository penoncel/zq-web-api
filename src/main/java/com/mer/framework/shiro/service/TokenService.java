package com.mer.framework.shiro.service;


import com.mer.common.redis.key.AppLoginKey;
import com.mer.common.redis.val.LoginUser;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.project.service.SysPermissionsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@Service
@Slf4j
public class TokenService {

    @Autowired
    private SysPermissionsServer permissionsServer;

    @Autowired
    private RedisService redisService;

    /**
     * 获取当前登录的User对象
     *
     * @param phone 手机号
     * @return
     */
    public LoginUser getLoginUser(String phone) {
        LoginUser loginUser = redisService.get(AppLoginKey.loginUserIinfo, phone, LoginUser.class);
        if (Objects.isNull(loginUser)) {
            loginUser = permissionsServer.getUserAutoAll(phone);
            redisService.set(AppLoginKey.loginUserIinfo, phone, loginUser);
            return loginUser;
        }
        return loginUser;
    }


}
