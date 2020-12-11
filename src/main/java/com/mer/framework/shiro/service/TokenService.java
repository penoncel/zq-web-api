package com.mer.framework.shiro.service;


import com.mer.common.enums.ErrorStateEnum;
import com.mer.common.rediskey.AppLoginKey;
import com.mer.common.utils.JwtUtil;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.web.domain.LoginUser;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysPermissionsServer;
import com.mer.project.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

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
     * @return User
     */
    public LoginUser getLoginUser(HttpServletRequest request){
        // 获取token
        String token = JwtUtil.getToken(request);
        // 获取手机号
        String phone = JwtUtil.getPhone(token);
        if(Objects.isNull(phone)){
            System.out.println("token 中 手机号为空 也是验证不通过");
            throw new IncorrectCredentialsException(ErrorStateEnum.TOKEN_ERROR.getMsg());
        }
        log.info(String.format(" 手机号 [%s]  - 获取权限",phone));
        // 获取缓存loginUser
        LoginUser loginUser = redisService.get(AppLoginKey.loginUserIinfo,phone,LoginUser.class);
        if (loginUser == null) {
            loginUser = new LoginUser();
            // 获取当前登录用户
            SysUser user = userServer.findByUserPhone(phone);
            loginUser.setUser(user);
            // 获取当前登录用户所有权限
            Set<String> permissionsSet = permissionsServer.getPermissionsSet(user.getId());
            loginUser.setPermissionsSet(permissionsSet);
            // 获取当前登录用户所有角色
            Set<String> roleSet =permissionsServer.getRoleSet(user.getId());
            loginUser.setRoleSet(roleSet);
            // 缓存当前登录用户
            redisService.set(AppLoginKey.loginUserIinfo,phone, loginUser);
            return loginUser;
        }
        return loginUser;
    }


}
