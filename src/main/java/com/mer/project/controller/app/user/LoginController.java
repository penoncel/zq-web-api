package com.mer.project.controller.app.user;


import com.mer.common.redis.key.AppLoginKey;
import com.mer.framework.annotction.LOG;
import com.mer.framework.annotction.PhoneNumber;
import com.mer.framework.annotction.RequestLimit;
import com.mer.framework.web.domain.Result;
import com.mer.project.service.SysUserService;
import com.mer.project.service.sms.SmsSandSendPo;
import com.mer.project.service.sms.TsmsSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @Program: zq-web-api
 * @Description: 用户登陆
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */

@Api(tags = "用户登陆授权")
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestLimit(maxCount = 5, second = 1)
public class LoginController {

    @Autowired
    ThreadPoolTaskExecutor notifyServiceThreadPool;
    @Autowired
    SysUserService userService;


    @ApiOperation("密码登录")
    @LOG(operModul = "用户登入", operType = "密码登录", operDesc = "手机号密码登入")
    @PostMapping("/login/pwdLogin")
    public Result pwdLogin(@ApiParam(value = "手机号", required = true) @PhoneNumber String phone,
                           @ApiParam(value = "密码(密码+用户名进行MD5加密)", required = true) @NotEmpty(message = "密码不能为空") String password,
                           @ApiParam(value = "版本号", required = true) @RequestParam(value = "versionCode") String versionCode,
                           @ApiParam(value = "设备类型(1：Android，2：iOS)", required = true) @RequestParam(value = "appType") String appType) throws InterruptedException {

        return userService.loginByPassword(phone, password);
    }


    @ApiOperation(value = "发送登录验证码")
    @LOG(operModul = "用户登入", operType = "验证码快捷登入)", operDesc = "发送登入验证码，用作验证码登入")
    @PostMapping("/code/getLoginCode")
    public Result getLoginCode(@ApiParam(value = "手机号", required = true) @PhoneNumber String phone) {
        notifyServiceThreadPool.execute(new TsmsSendService(new SmsSandSendPo(AppLoginKey.smsCode,phone, "短信验证码：")));
        return Result.success();
    }

    @ApiOperation("验证码登录(获取登入验证码)")
    @LOG(operModul = "用户登入", operType = "验证码登入", operDesc = "通过验证码进行登入")
    @PostMapping("/login/codeLogin")
    public Result codeLogin(@ApiParam(value = "手机号", required = true) @PhoneNumber String phone,
                            @ApiParam(value = "验证码", required = true) @NotEmpty(message = "验证码不能为空") String code,
                            @ApiParam(value = "版本号", required = true) @RequestParam(value = "versionCode", required = false) String versionCode,
                            @ApiParam(value = "设备类型（1：Android，2：iOS）", required = true) @RequestParam(value = "appType", required = false) String appType) {
        return userService.loginByCode(phone, code);
    }


    @ApiOperation("发送验证码(修改密码)")
    @LOG(operModul = "用户登入", operType = "发送验证码(修改密码)", operDesc = "发送验证码,通过验证码修改密码")
    @PostMapping("/code/modifyPasswordCode")
    public Result sendModifyPasswordCode(@ApiParam(value = "手机号", required = true) @PhoneNumber String phone) {
        notifyServiceThreadPool.execute(new TsmsSendService(new SmsSandSendPo(AppLoginKey.modifyPassWordCode,phone, "短信验证码：")));
        return Result.success();
    }

    @ApiOperation("通过验证码修改密码")
    @LOG(operModul = "用户登入", operType = "修改密码", operDesc = "通过验证码修改密码")
    @PostMapping("/modifyPassword")
    public Result modifyPassword(@ApiParam(value = "手机号", required = true) @PhoneNumber String phone,
                                 @ApiParam(value = "验证码", required = true) @NotEmpty(message = "验证码不能为空") String code,
                                 @ApiParam(value = "新密码", required = true) @NotEmpty(message = "新密码不能为空") String password) {
        return userService.modifyPassword(phone, code, password);
    }

}
