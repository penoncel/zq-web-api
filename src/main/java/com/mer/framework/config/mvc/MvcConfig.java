package com.mer.framework.config.mvc;

import com.mer.framework.web.interceptor.AppConnectBushIntercept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Program: zq-web-api
 * @Description: Web配置
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private AppConnectBushIntercept appConnectBushIntercept;

    /**
     * 不拦截的
     */
    private final String[] excludePathPatterns = new String[]{"/", "/webjars/**", "/swagger-resources/**", "/v2/**", "/doc.html"};

    /**
     * 设置跨域访问
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                //可以被跨域的路径，”/**”表示无限制。
                .addMapping("/**")
                //”*”允许所有的请求域名访问跨域资源，当设置具体URL时只有被设置的url可以跨域访问。例如：allowedOrigins(“https://www.baidu.com”),则只有https://www.baidu.com能访问跨域资源。
                .allowedOrigins("*")
                //允许跨域访问资源服务器的请求方式，如：POST、GET、PUT、DELETE等，“*”表示无限制。
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                //是否允许用户发送、处理 cookie
                .allowCredentials(true)
                //预检请求的有效期，单位为秒。有效期内，不会重复发送预检请求
                .maxAge(3600 * 24);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appConnectBushIntercept).addPathPatterns("/**").excludePathPatterns(excludePathPatterns);
        log.info("Init AppConnectBushIntercept Success。。。。");
    }


}
