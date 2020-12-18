package com.mer.project.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @Program: zq-web-api
 * @Description: 权限Dao接口
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Mapper
public interface SysPermissionsDao {

    /**
     * 根据用户 id获取其角色权限
     *
     * @param userId 角色id
     * @return Set<String>
     */
    @Select(" SELECT " +
            "   sm.href " +
            " FROM sys_role sr " +
            " LEFT JOIN sys_role_permission srp ON sr.id = srp.role_id " +
            " LEFT JOIN `sys_menu`sm ON srp.menu_id = sm.id " +
            " WHERE  " +
            " srp.role_id IN " +
            " ( " +
            "     SELECT " +
            "       sur.role_id " +
            "     FROM sys_user sy " +
            "     LEFT JOIN sys_user_roles sur ON sy.id=sur.user_id " +
            "     WHERE sy.id = #{userId} " +
            " )" +
            " GROUP BY sm.href  ")
    Set<String> getPermissionsSet(@Param("userId") Integer userId);

    /**
     * 根据用户id 获取其角色 key 列表
     *
     * @param userId userId
     * @return Set<Role>
     */
    @Select("select r.role_key from sys_role r inner join sys_user_roles ur on r.id = ur.role_id where ur.user_id  = #{userId}")
    Set<String> getRoleSet(@Param("userId") Integer userId);

    /**
     * 添加用户角色权限
     *
     * @param userId
     * @param roleIds
     * @return
     */
    int addRole(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);
}
