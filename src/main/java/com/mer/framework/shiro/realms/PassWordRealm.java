package com.mer.framework.shiro.realms;


import com.mer.common.redis.val.LoginUser;
import com.mer.framework.shiro.service.TokenService;
import com.mer.framework.shiro.token.CustomizedToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 密码登入认证
 *
 * @author zhaoqi
 * @date 2020/5/20 17:20
 **/
@Slf4j
public class PassWordRealm extends AuthorizingRealm {

    @Autowired
    TokenService tokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomizedToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 获取身份认证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomizedToken token = (CustomizedToken) authenticationToken;
        String phone = token.getUsername();
        LoginUser loginUser = tokenService.getLoginUser(phone);
        if (loginUser.getUser() == null) {
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                phone,
                loginUser.getUser().getPassword(),
                this.getClass().getName()
        );
    }
}
