package com.mer.project.controller.app;

import com.mer.framework.annotction.LOG;
import com.mer.framework.web.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @Program: zq-web-api
 * @Description: 测试控制器
 * @Author: 赵旗
 * @Create: 2020-12-21 13:34
 */
@Api(tags = "测试控制器")
@RestController
@RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @ApiOperation("getOne")
    @LOG(operModul = "测试控制器",operType = "getOne",operDesc = "---")
    @PostMapping("/test/getOne")
    public Result getOne(@RequestParam @ApiParam(value = "id", required = true)String id){
        System.out.println("getOne进来:"+id);
        return Result.success(id);
    }

    @ApiOperation("testOne")
    @LOG(operModul = "测试控制器",operType = "testOne",operDesc = "---")
    @PostMapping("/apitest/testOne")
    public Result testOne(@RequestParam @ApiParam(value = "id", required = true)String id){
        System.out.println("testOne进来:"+id);
        return Result.success(id);
    }


}
