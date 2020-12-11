package com.mer.project.dao;

import com.mer.project.pojo.SysUser;
import org.apache.ibatis.annotations.*;

/**
 * @Program: zq-web-api
 * @Description: 用户Dao接口
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Mapper
public interface SysUserDao {
    /**
     * 修改用户密码
     * @param sysUser
     * @return
     */
    @Update("update sys_user set password=#{password},salt=#{salt} where phone=#{phone}")
    boolean updatePassWorld(SysUser sysUser);

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    @Insert("insert into sys_user(phone,salt,password,icon,name,address,reg_times,sex,age) values(#{phone},#{salt},#{password},#{icon},#{name},#{address},#{reg_times},#{sex},#{age})")
    @Options(useGeneratedKeys=true, keyProperty = "id",keyColumn="id")
    int insertLoginUserMsg(SysUser sysUser);

    /**
     * 根据手机号查找用户
     * @param phone
     * @return
     */
    @Select("select * from sys_user where phone = #{phone}")
    SysUser findByUserPhone(@Param("phone") String phone);
}
