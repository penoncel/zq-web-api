package com.mer.project.service.impl;

import com.mer.project.dao.SysPermissionsDao;
import com.mer.project.service.SysPermissionsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    @Override
    public Set<String> getPermissionsSet(Integer userId) {
        Set<Integer> roleIdSet = this.getRoleIdSet(userId);
        Set<String> strings = new HashSet<>();
        for (Integer roleId : roleIdSet) {
            Set<String> permissionsSetByRoleId = getPermissionsSetByRoleId(roleId);
            strings.addAll(permissionsSetByRoleId);
        }
        // 去除空权限
        strings.remove("");
        return strings;
    }

    /**
     * 根据角色id获取权限列表
     * @param roleId 角色id
     * @return 权限列表
     */
    public Set<String> getPermissionsSetByRoleId(Integer roleId){
        return permissionsDao.getPermissionsSet(roleId);
    }

    @Override
    public Set<Integer> getRoleIdSet(Integer userId) {
        return permissionsDao.getRoleIdSet(userId);
    }

    @Override
    public Set<String> getRoleSet(Integer userId) {
        return permissionsDao.getRoleSet(userId);
    }

    @Override
    public int addRole(Integer userId, Integer[] roleIds) {
      return  permissionsDao.addRole(userId,roleIds);
    }
}
