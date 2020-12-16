package com.mer.framework.shiro.realms;

import com.mer.common.redis.key.AppLoginKey;
import com.mer.common.redis.val.LoginUser;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.service.TokenService;
import com.mer.framework.shiro.token.CustomizedToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 验证码登入 认证
 *
 * @author lixiao
 * @date 2019/7/31 11:40
 */
@Slf4j
public class CodeRealm extends AuthorizingRealm {

    @Autowired
    RedisService redisService;

    @Autowired
    TokenService tokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomizedToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 获取身份认证信息
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        CustomizedToken token = (CustomizedToken) auth;
        String phone = token.getUsername();
        String redisCode = redisService.getString(AppLoginKey.smsCode, phone);
        if (redisCode == null) {
            throw new ExpiredCredentialsException();
        }
        LoginUser loginUser = tokenService.getLoginUser(phone);
        if (loginUser.getUser() == null) {
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                phone,
                redisCode,
                this.getClass().getName()
        );
    }
}
