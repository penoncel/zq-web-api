package com.mer.project.controller.app;

import com.mer.framework.web.controller.BaseController;
import com.mer.framework.web.domain.Result;
import com.mer.project.pojo.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/**
 * @Program: zq-web-api
 * @Description: 用户Service接口
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Api(tags = "【user】用户")
@RestController
@RequestMapping("/app/test")
@Validated
public class Test extends BaseController {

    @ApiOperation("测试")
    @RequestMapping("/get")
    public Object get(){

        return "get";
    }


    @ApiOperation("退出")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string")
    @GetMapping("/logout")
    @RequiresRoles(logical = Logical.OR,value = {"admin","user"})
    @RequiresPermissions(logical = Logical.OR, value = {"user", "admin","WebUser/edit"})
    public Result logout(@NotEmpty(message = "phone 不能为空") String phone) {
        // 退出操作
        return Result.success(phone);
    }

    @ApiOperation("退出")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string")
    @GetMapping("/deleted/{userId}")
    @RequiresPermissions(logical = Logical.OR, value = {"user", "admin","WebUser/edit"})
    public Result deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId){
        return Result.success(userId);
    }

    @ApiOperation(value = "获取所有用户[permissions: system:user:list]", response = SysUser.class)
    @GetMapping("/ddd/{userId}")
    @RequiresPermissions(logical = Logical.OR, value = {"user", "admin","WebUser/edit"})
    public Result dddd(@PathVariable @NotNull(message = "userId不能为空") Integer userId){
        return Result.success(userId);
    }
}
