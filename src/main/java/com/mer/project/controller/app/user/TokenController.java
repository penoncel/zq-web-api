package com.mer.project.controller.app.user;

import com.mer.framework.annotction.LOG;
import com.mer.framework.annotction.RequestLimit;
import com.mer.framework.web.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;


/**
 * @Program: zq-web-api
 * @Description: 刷新TokenApi
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */

@Api(tags = "用户刷新Token接口")
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/app/refToken",produces = MediaType.APPLICATION_JSON_VALUE)
@RequestLimit(maxCount = 5,second = 1)
public class TokenController {

    @ApiOperation("通过refreshToken参数进行刷新Token")
    @LOG(operModul = "用户模块",operType = "刷新Token)",operDesc = "获得 refreshToken,和 token的过期时间,进行系统删除redis中的缓存 重新签发 token信息")
    @GetMapping("/refreshToken")
    public Result refreshToken(@ApiParam(value = "token", required = true) @NotEmpty(message = "authorization不能为空(refreshToken)")String token) throws Exception {
        System.out.println("进来"+token);
        return Result.success();
    }


}
