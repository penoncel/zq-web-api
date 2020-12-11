package com.mer.framework.shiro.realms;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mer.common.enums.ErrorStateEnum;
import com.mer.common.rediskey.AppLoginKey;
import com.mer.common.utils.JwtUtil;
import com.mer.common.utils.ServletUtils;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.service.TokenService;
import com.mer.framework.shiro.token.JwtToken;
import com.mer.framework.web.domain.LoginUser;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Jwt 认证
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(JwtRealm.class);


    @Resource
    private TokenService tokenService;

    @Resource
    private SysUserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        authorizationInfo.addRoles(loginUser.getRoleSet());
        // 添加权限
        authorizationInfo.addStringPermissions(loginUser.getPermissionsSet());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        logger.info("====================Token认证====================");
        String token = (String) auth.getCredentials();
        String phone = null;

        //token 认证
        try{
            boolean verify = JwtUtil.verify(token);
            if(verify){
                phone = JwtUtil.getPhone(token);
                if (StringUtils.isEmpty(phone)) {
                    JSONObject o = new JSONObject();
                    o.put("code", ErrorStateEnum.TOKEN_PHONE_IS_NULL.getCode());
                    o.put("msg",  ErrorStateEnum.TOKEN_PHONE_IS_NULL.getMsg());
                    throw new AuthenticationException(o.toString());
                }
            }
        } catch (JWTDecodeException e){
            JSONObject o = new JSONObject();
            o.put("code", ErrorStateEnum.TOKEN_ERROR.getCode());
            o.put("msg",  ErrorStateEnum.TOKEN_ERROR.getMsg());
            throw new AuthenticationException(o.toString());
        }catch (TokenExpiredException e){
            JSONObject o = new JSONObject();
            o.put("code", ErrorStateEnum.TOKEN_INVALID.getCode());
            o.put("msg",  ErrorStateEnum.TOKEN_INVALID.getMsg());
            throw new AuthenticationException(o.toString());
        }catch (IllegalArgumentException e){
            JSONObject o = new JSONObject();
            o.put("code", ErrorStateEnum.TOKEN_ERROR.getCode());
            o.put("msg",  ErrorStateEnum.TOKEN_ERROR.getMsg());
            throw new AuthenticationException(o.toString());
        }


        logger.info(String.format(" 手机号 [%s] header in X-Ttoken 身份认证方法 - code auth start...",phone));


        LoginUser loginUser = null;
        if(redisService.exists(AppLoginKey.loginUserIinfo,phone)){
            loginUser = redisService.get(AppLoginKey.loginUserIinfo,phone, LoginUser.class);
        }else{
            SysUser user  = userService.findByUserPhone(phone);
            if(user!=null){
                loginUser = redisService.setLogingUserRedis(user);
            }
        }

        if (loginUser.getUser() == null) {
            JSONObject o = new JSONObject();
            o.put("code", ErrorStateEnum.TOKEN_PHONE_POJO_IS_NULL.getCode());
            o.put("msg",  ErrorStateEnum.TOKEN_PHONE_POJO_IS_NULL.getMsg());
            throw new AuthenticationException(o.toString());
        }

        return new SimpleAuthenticationInfo(
                token,
                token,
                this.getClass().getName()
        );
    }
}
