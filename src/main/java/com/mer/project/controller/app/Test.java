package com.mer.project.controller.app;

import com.mer.framework.annotction.PhoneNumber;
import com.mer.framework.web.controller.BaseController;
import com.mer.framework.web.domain.Result;
import com.mer.project.pojo.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "获取所有用户[permissions: a:b:c]", response = SysUser.class)
    @GetMapping("/ddds/{userId}")
    @RequiresPermissions(logical = Logical.OR, value = {"1","user","WebRole/power"})
    public Result dddd(@PathVariable @NotNull(message = "userId不能为空") Integer userId){
        return Result.success(userId);
    }


    @ApiOperation(value = "获取所有用户[permissions: a:b:c]", response = SysUser.class)
    @GetMapping("/ddd")
    @RequiresPermissions(logical = Logical.OR, value = {"1","user","WebRole/power"})
    public Result dddd(@ApiParam(value = "手机号", required = true)  @PhoneNumber String phone){
        return Result.success(phone);
    }
}
