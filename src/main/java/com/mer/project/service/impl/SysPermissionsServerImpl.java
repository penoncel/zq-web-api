package com.mer.project.service.impl;

import com.mer.common.redis.val.LoginUser;
import com.mer.project.dao.SysPermissionsDao;
import com.mer.project.dao.SysUserDao;
import com.mer.project.pojo.SysUser;
import com.mer.project.service.SysPermissionsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Program: zq-web-api
 * @Description: 权限ServiceImpl
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Service
public class SysPermissionsServerImpl implements SysPermissionsServer {

    @Autowired
    private SysPermissionsDao permissionsDao;
    @Autowired
    private SysUserDao userDao;

    @Override
    public LoginUser getUserAutoAll(String phone) {
        LoginUser loginUser = new LoginUser();
        //用户
        SysUser user = userDao.findByUserPhone(phone);
        //角色
        Set<String> roles = this.getRoleSet(user.getId());
        //权限
        Set<String> permissions = this.getPermissionsSet(user.getId());
        loginUser.setUser(user);
        loginUser.setRoleSet(roles);
        loginUser.setPermissionsSet(permissions);
        return loginUser;
    }

    @Override
    public Set<String> getPermissionsSet(Integer userId) {
        return permissionsDao.getPermissionsSet(userId);
    }

    @Override
    public Set<String> getRoleSet(Integer userId) {
        return permissionsDao.getRoleSet(userId);
    }

    @Override
    public int addRole(Integer userId, Integer[] roleIds) {
        return permissionsDao.addRole(userId, roleIds);
    }
}
