//package com.mer.project.controller.app;
//
//import com.mer.framework.annotction.LOG;
//import com.mer.framework.annotction.PhoneNumber;
//import com.mer.framework.annotction.RequestLimit;
//import com.mer.framework.web.controller.BaseController;
//import com.mer.framework.web.domain.Result;
//import com.mer.project.pojo.SysUser;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.constraints.NotNull;
//
///**
// * @Program: zq-web-api
// * @Description: 用户Service接口
// * @Author: 赵旗
// * @Create: 2020-12-09 12:08
// */
//@Api(tags = "Test")
//@RestController
//@RequestMapping("/app/test")
//public class Test  {
//
//    @ApiOperation("get2")
//    @RequestMapping("/2/get2")
//    public Object get2(){
//        return "/app/test/2/get2";
//    }
//
//    @ApiOperation("get")
//    @RequestMapping("/get")
//    public Object get() {
//        Integer.parseInt("123aeq");
//        return "get";
//    }
//
//}
