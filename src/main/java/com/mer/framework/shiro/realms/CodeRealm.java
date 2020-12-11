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
 * 验证码登入 认证
 * @author lixiao
 * @date 2019/7/31 11:40
 */
@Slf4j
public class CodeRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(CodeRealm.class);

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomizedToken;
    }

    /**
     * 获取授权信息
     * @param principals principals
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 获取身份认证信息
     * @param auth authenticationToken
     * @return AuthenticationInfo
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        CustomizedToken token = (CustomizedToken) auth;
        String  phone = token.getUsername();

        /**
         * 这里验证码 和 user对象 都可以不做验证
         * 已经在 service 中进行过验证
         * 先验证了，验证码是否正确，正确了说明手机号也是正确的
         * 即可进行用户对象验证，不存在则自动注册。
         */
        logger.info(String.format(" 手机号 [%s] 验证码身份认证方法 - code auth start...",phone));
        // 从redis中获取登录验证码
        String redisCode =  redisService.getString(AppLoginKey.smsCode,token.getUsername());
        if (redisCode == null) {
            throw new ExpiredCredentialsException();
        }

        /**
         * 前面在 impl中已经进行过用户信息 1 rdis、2数据库验证
         * 所以这里只针对redis中进行查询
         * 问题：
         *      如果有人干掉了redis缓存，则需要进行数据库验证，双向验证
         */
        LoginUser loginUser = null;
        if(redisService.exists(AppLoginKey.loginUserIinfo,token.getUsername())){
            loginUser = redisService.get(AppLoginKey.loginUserIinfo,token.getUsername(), LoginUser.class);
        }else{
            SysUser user  = userService.findByUserPhone(token.getUsername());
            if(user!=null){
                loginUser = redisService.setLogingUserRedis(user);
            }
        }
        if (loginUser.getUser() == null) {
            // 抛出账号不存在异常
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                loginUser.getUser(),
                redisCode,
                this.getClass().getName()
        );
    }
}
