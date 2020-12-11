package com.mer.framework.config;

import com.mer.common.filter.MyFilter;
import com.mer.framework.shiro.filter.JwtFilter;
import com.mer.framework.shiro.realms.CodeRealm;
import com.mer.framework.shiro.realms.JwtRealm;
import com.mer.framework.shiro.realms.PassWordRealm;
import com.mer.framework.shiro.realms.UserModularRealmAuthenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Program: zq-web-api
 * @Description: Shiro配置
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Configuration
public class ShiroConfig {

    /**
     * 开启shiro权限注解
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator creator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager 安全管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 密码登录时使用该匹配器进行匹配
     * @return HashedCredentialsMatcher
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 设置哈希算法名称   SHA-256
        matcher.setHashAlgorithmName("md5");
        // 设置哈希迭代次数  1024
        matcher.setHashIterations(2);
        // 设置存储凭证十六进制编码
        return matcher;
    }









    /**
     * Shiro内置过滤器，可以实现拦截器相关的拦截器
     *    常用的过滤器：
     *      anon：无需认证（登录）可以访问
     *      authc：必须认证才可以访问
     *      user：如果使用rememberMe的功能可以直接访问
     *      perms：该资源必须得到资源权限才可以访问
     *      role：该资源必须得到角色权限才可以访问
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filter = new LinkedHashMap<>(2);
        //设置我们自定义的JWT过滤器
        filter.put("jwt", new JwtFilter());
        //设置我们自定义的 参数 sql 过滤器 过滤 非法参数 SQL注入 和 攻击
        filter.put("repeat", new MyFilter());
        bean.setFilters(filter);
        // 设置 SecurityManager
        bean.setSecurityManager(securityManager);


        //拦截器
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 放行 login
        filterMap.put("/app/login/**",   "anon");
        //放行 静态资源
        filterMap.put("/images/**",  "anon");
        filterMap.put("/doc.html", "anon");
        filterMap.put("/app/apitest/**", "anon");

        //swagger配置放行
//        filterMap.put("/swagger-ui.html","anon");
        filterMap.put("/swagger/**","anon");
        filterMap.put("/webjars/**","anon");
        filterMap.put("/swagger-resources/**","anon");
        filterMap.put("/v2/**","anon");

        // 所有请求必须要先通过参数过滤
        filterMap.put("/**", "repeat,jwt");
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }



    /**
     *  密码登入 - 认证器
     *  @return
     * **/
    @Bean
    public PassWordRealm passWordRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        PassWordRealm userRealm = new PassWordRealm();
        //加密器
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    /**
     * 验证码登录Realm
     * @param matcher 密码匹配器
     * @return CodeRealm
     */
    @Bean
    public CodeRealm codeRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        CodeRealm codeRealm = new CodeRealm();
        codeRealm.setCredentialsMatcher(matcher);
        return codeRealm;
    }

    /**
     * jwtRealm
     * @return JwtRealm
     */
    @Bean
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }


    @Bean
    public UserModularRealmAuthenticator userModularRealmAuthenticator(){
        // 自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }


    /**
     *  SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     */
    @Bean
    public SecurityManager securityManager(@Qualifier("passWordRealm") PassWordRealm passwordReal,
                                           @Qualifier("codeRealm") CodeRealm codeRealm,
                                           @Qualifier("jwtRealm") JwtRealm jwtRealm,
                                           @Qualifier("userModularRealmAuthenticator") UserModularRealmAuthenticator userModularRealmAuthenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setAuthenticator(userModularRealmAuthenticator);

        // 设置realm
        List<Realm> realms = new ArrayList<>();
        // 添加多个realm
        realms.add(passwordReal);
        realms.add(codeRealm);
        realms.add(jwtRealm);

        securityManager.setRealms(realms);
        /*
         * 关闭shiro自带的session，详情见文档
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }
}
