package com.mer.framework.shiro.realms;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mer.common.enums.SysMsgEnum;
import com.mer.common.redis.val.LoginUser;
import com.mer.common.utils.JwtUtil;
import com.mer.framework.shiro.service.TokenService;
import com.mer.framework.shiro.token.JwtToken;
import com.mer.framework.web.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * Jwt 认证
 *
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 用户授权权限 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     *
     * @param principals 权限
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String phone = (String) principals.getPrimaryPrincipal();
        LoginUser loginUser = tokenService.getLoginUser(phone);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        authorizationInfo.addRoles(loginUser.getRoleSet());
        // 添加权限
        authorizationInfo.addStringPermissions(loginUser.getPermissionsSet());
//        log.warn(" 用户 [{}] 配置权限 .........",phone);
        return authorizationInfo;
    }

    /**
     * 认证用户
     *
     * @param auth 用户
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String phone = null;
        try {
            boolean verify = JwtUtil.verify(token);
            if (verify) {
                phone = JwtUtil.getPhone(token);
                if (StringUtils.isEmpty(phone)) {
                    throw new AuthenticationException(Result.tJson(SysMsgEnum.TOKEN_ERROR));
                }
            }
        } catch (TokenExpiredException e) {
            throw new AuthenticationException(Result.tJson(SysMsgEnum.TOKEN_INVALID));
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(Result.tJson(SysMsgEnum.TOKEN_ERROR));
        }
        LoginUser loginUser = tokenService.getLoginUser(phone);
        if (loginUser.getUser() == null) {
            throw new AuthenticationException(Result.tJson(SysMsgEnum.TOKEN_ERROR));
        }
        return new SimpleAuthenticationInfo(
                phone,
                token,
                this.getClass().getName()
        );
    }
}
