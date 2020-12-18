package com.mer.project.service;


import com.mer.common.redis.val.LoginUser;

import java.util.Set;

/**
 * @Program: zq-web-api
 * @Description: 权限Service
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public interface SysPermissionsServer {

    /**
     * 获得用户相关信息
     *
     * @param phone 手机号
     * @return
     */
    LoginUser getUserAutoAll(String phone);

    /**
     * 根据用户 id获取其角色权限
     *
     * @param userId 角色id
     * @return Set<String>
     */
    Set<String> getPermissionsSet(Integer userId);

    /**
     * 根据用户id获取其角色key列表
     *
     * @param userId userId
     * @return Set<Role>
     */
    Set<String> getRoleSet(Integer userId);

    /**
     * 添加用户角色权限
     *
     * @param userId
     * @param roleIds
     * @return
     */
    int addRole(Integer userId, Integer... roleIds);
}
