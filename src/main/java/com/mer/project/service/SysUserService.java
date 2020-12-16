package com.mer.project.service;

import com.mer.framework.web.domain.Result;
import com.mer.project.pojo.SysUser;

/**
 * @Program: zq-web-api
 * @Description: 用户Service接口
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public interface SysUserService {
    /**
     * 手机号，密码  登入
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    Result loginByPassword(String phone, String password);


    /**
     * 手机号，验证码  快捷登入
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    Result loginByCode(String phone, String code);


    /**
     * 通过短信验证码进行 修改密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return
     */
    Result modifyPassword(String phone, String code, String password);


    /**
     * 用户注册
     *
     * @param phone 手机号
     * @param args  密码
     * @return
     */
    Boolean registerLoginUser(String phone, String... args);

    /**
     * 根据手机号查找用户
     *
     * @param phone
     * @return
     */
    SysUser findByUserPhone(String phone);

}
