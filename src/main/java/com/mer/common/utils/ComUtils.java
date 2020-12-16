package com.mer.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

/**
 * @Program: zq-web-api
 * @Description: 公共工具
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Slf4j
public class ComUtils {

    /**
     * 获取六位数验证码
     *
     * @return 验证码
     */
    public static int getCode() {
        return (int) ((Math.random() * 9 + 1) * 1000);
    }


    /**
     * 使用SHA256加密
     *
     * @param password 需要加密的密码
     * @return 返回加密后的密码
     */
    public static String encryptPassword(String password) {
        return String.valueOf(new SimpleHash("md5", password, null, 2));
    }


    /**
     * 获取uuid
     *
     * @return string
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * input e
     *
     * @param e
     * @return
     */
    public static String writerEx(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 获取request
     */
    private static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String outStr(HttpServletResponse response, String string) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(string);
            log.warn(" ==> " + string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}