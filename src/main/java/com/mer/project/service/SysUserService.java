package com.mer.project.service;

import com.aliyuncs.exceptions.ClientException;
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
     * 用户注册
     * @param phone
     * @param args
     * @return
     */
    Boolean registerLoginUser(String phone, String... args);

    /**
     * 通过短信修改密码
     * @param phone
     * @param code
     * @param password
     * @return
     */
    Result modifyPassword(String phone, String code, String password);

    /**
     * 手机号，验证码  登入
     * @param phone
     * @param code
     * @return
     */
    Result loginByCode(String phone, String code);

    /**
     * 发送修改密码  验证码
     * @param phone
     * @throws ClientException
     */
    void sendModifyPasswordCode(String phone) throws ClientException;

    /**
     * 发送短信验证码
     * @param phone
     * @throws ClientException
     */
    void sendLoginCode(String phone) throws ClientException;

    /**
     * 手机号，密码  登入
     * @param phone
     * @param password
     * @return
     */
    Result loginByPassword(String phone, String password);

    /**
     * 根据手机号查找用户
     * @param phone
     * @return
     */
    SysUser findByUserPhone(String phone);

}
