package com.mer.framework.shiro.service;


import com.mer.common.redis.key.AppLoginKey;
import com.mer.common.redis.val.LoginUser;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.project.service.SysPermissionsServer;
import com.mer.project.service.SysUserService;
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
    private SysUserService userServer;

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
        // 获取 reids loginUser
        LoginUser loginUser = redisService.get(AppLoginKey.loginUserIinfo, phone, LoginUser.class);
        if (Objects.isNull(loginUser)) {
            loginUser = new LoginUser();
            // 用户
            loginUser.setUser(userServer.findByUserPhone(phone));
            // 角色
            loginUser.setPermissionsSet(permissionsServer.getPermissionsSet(loginUser.getUser().getId()));
            // 权限
            loginUser.setRoleSet(permissionsServer.getRoleSet(loginUser.getUser().getId()));
            // 缓存当前登录 对象
            redisService.set(AppLoginKey.loginUserIinfo, phone, loginUser);
            return loginUser;
        }
        return loginUser;
    }


}
