package com.mer.framework.shiro.filter;

import com.mer.common.constant.Constant;
import com.mer.common.enums.SysMsgEnum;
import com.mer.common.redis.key.AppLoginKey;
import com.mer.common.utils.ComUtils;
import com.mer.common.utils.SpringContextUtils;
import com.mer.framework.config.redis.redisservice.RedisService;
import com.mer.framework.shiro.token.JwtToken;
import com.mer.framework.web.domain.Result;
import com.mer.project.vo.resp.LoginTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * JWt 过滤器
 *
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证
     *
     * @param servletRequest  ServletRequest
     * @param servletResponse ServletResponse
     * @param mappedValue     mappedValue
     * @return 是否成功
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (Objects.nonNull(request.getHeader(Constant.TOKEN_HEADER_NAME))) {
            return executeLogin(request, response);
        } else {
            log.warn("request Header {} is null ,Uri：[{}] , method type：[{}]", Constant.TOKEN_HEADER_NAME, request.getRequestURI(), request.getMethod());
            ComUtils.outStr(request, response, Result.tJson(SysMsgEnum.NOT_AUTH));
            return false;
        }
    }


    /**
     * 执行 token 认证
     *
     * @param request
     * @param response
     * @return
     */
    protected boolean executeLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            JwtToken jwtToken = new JwtToken(request.getHeader(Constant.TOKEN_HEADER_NAME));
            // 提交给 jwtrealm 进行token认证 , 如果错误他会抛出异常并被捕获
            getSubject(request, response).login(jwtToken);

            /**
             * 单点登入（到这里代表token认证无误）
             */
            Subject subject = getSubject(request, response);
            // 当前用户
            String username = subject.getPrincipal().toString();
            //注册 bean
            RedisService redisService = SpringContextUtils.getBean(RedisService.class);
            //获取 RD 模版
            LoginTokenVo redisToken = redisService.get(AppLoginKey.userToken, username, LoginTokenVo.class);
            /**
             * 1、每次登入的时候，都会注销上一次登入 未过期的用户，且重新生成缓存
             * 2、即视为，存在缓存，Token 一致，说明当前用户登入，如果存在，且tonken不一致，说明另外一个是被登入。
             * 3、这里不会出现 redis过期了还过来验证。shiro中过滤器 顺序执行。
             */
            if (Objects.nonNull(redisToken)) {
                if (!jwtToken.getPrincipal().equals(redisToken.getToken())) {
                    subject.logout();
                    // Todo 这里提示 可以带上 是几点 在哪个设备上登入了（信息来源于token）
                    ComUtils.outStr(request, response, Result.tJson(SysMsgEnum.THE_ONLY_ACCESS));
                    return false;
                }
            }
        } catch (AuthenticationException e) {
            ComUtils.outStr(request, response, e.getMessage());
            return false;
        } catch (Exception e) {
            ComUtils.outStr(request, response, Result.tJson(SysMsgEnum.INTERNAL_SERVER_ERROR));
            return false;
        }
        return true;
    }


}
