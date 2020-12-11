package com.mer.framework.shiro.realms;


import com.mer.common.rediskey.AppLoginKey;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.token.CustomizedToken;
import com.mer.framework.web.domain.LoginUser;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 密码登入认证
 * @author zhaoqi
 * @date 2020/5/20 17:20
 **/
@Slf4j
public class PassWordRealm  extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(PassWordRealm.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取授权信息
     * @param principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //这里只是密码登入，不做权限认证，直接返回
        return null;
    }

    /**
     * 获取身份认证信息
     * @param  authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomizedToken token = (CustomizedToken) authenticationToken;
        String  phone = token.getUsername();
        logger.info(String.format(" 手机号 [%s] 密码身份认证方法 - password auth start...",phone));
        //根据用户名获取用户信息

        LoginUser loginUser = null;
        SysUser user =null;
        if(redisService.exists(AppLoginKey.loginUserIinfo,token.getUsername())){
            loginUser = redisService.get(AppLoginKey.loginUserIinfo,token.getUsername(), LoginUser.class);
            user = loginUser.getUser();
        }else{
             user  = userService.findByUserPhone(token.getUsername());
            if(user!=null){
                loginUser = redisService.setLogingUserRedis(user);
            }
        }
        if(user==null){
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                loginUser.getUser(),
                loginUser.getUser().getPassword(),
                this.getClass().getName()
        );
    }
}
