package com.mer.project.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Program: zq-web-api
 * @Description: 用户信息
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Data
@TableName("sys_user")
@ApiModel(value="User对象", description="用户信息")
public class SysUser extends Model<SysUser> {

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "盐值", hidden = true)
    private String salt;

    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "姓名（昵称）")
    private String name;

    @ApiModelProperty(value = "地区")
    private String address;

    @ApiModelProperty(value = "注册时间")
    private String regTimes;

    @ApiModelProperty(value = "年龄")
    private int age;

    @ApiModelProperty(value = "性别")
    private int sex;

}
