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
     * 根据角色id获取其权限
     * @param roleId 角色id
     * @return Set<String>
     */
    @Select("select mu.href from sys_menu mu INNER join (  " +
            " select menu_id from sys_user_roles ur INNER join sys_role_permission rp on ur.role_id = rp.role_id and ur.role_id = #{roleId} " +
            " )t on mu.id = t.menu_id")
    Set<String> getPermissionsSet(@Param("roleId") Integer roleId);

    /**
     * 根据用户id 获取其角色 id 列表
     * @param userId userId
     * @return Set<Integer>
     */
    @Select("select role_id from sys_user_roles where user_id = #{userId}")
    Set<Integer> getRoleIdSet(@Param("userId")Integer userId);

    /**
     * 根据用户id 获取其角色 key 列表
     * @param userId userId
     * @return Set<Role>
     */
    @Select("select r.role_key from sys_role r inner join sys_user_roles ur on r.id = ur.role_id where ur.user_id  = #{userId}")
    Set<String> getRoleSet(@Param("userId") Integer userId);

    /**
     * 添加用户角色权限
     * @param userId
     * @param roleIds
     * @return
     */
    int addRole(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);
}
