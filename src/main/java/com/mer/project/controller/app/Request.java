package com.mer.project.controller.app;

import com.mer.framework.annotction.RequestLimit;
import com.mer.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Program: zq-web-api
 * @Description: 【user】用户
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Api(tags = "【user】用户")
@RestController
@RequestMapping("/app/apitest")
@Validated
public class Request extends BaseController {

    @RequestLimit(second = 60,maxCount = 5)
    @RequestMapping("/get")
    public Object get(){

        return "get";
    }

}
