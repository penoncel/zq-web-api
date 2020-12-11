package com.mer.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Program: zq-web-api
 * @Description: 过滤器配置
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Component
@WebFilter(filterName="MyFilter",urlPatterns="/*")
@Slf4j
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //sql,xss过滤
        /**
         * log.info("XssFilter.......orignal url:{},ParameterMap:{}",request.getRequestURI(), JSONObject.toJSONString(request.getParameterMap()));
         */
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper=new XssHttpServletRequestWrapper( request);
        filterChain.doFilter(xssHttpServletRequestWrapper, response);
        /**
         * log.info("XssFilter..........doFilter url:{},ParameterMap:{}",xssHttpServletRequestWrapper.getRequestURI(), JSONObject.toJSONString(xssHttpServletRequestWrapper.getParameterMap()));
          */
    }

    @Override
    public void destroy() {
    }
}
