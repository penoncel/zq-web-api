package com.mer.framework.aop.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Program: zq-web-api
 * @Description: 系统日志
 * @Author: 赵旗
 * @Create: 2020-12-18 10:58
 */
@Data
@TableName("web_operation_log")
public class LogPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    //
    private Integer id;
    // 用户名
    private String userName;
    // 硬件设备
    private String device;
    // 设备系统
    private String deviceSys;
    // 设备版本
    private String deviceV;
    // 操作模块
    private String operModule;
    // 操作类型
    private String operType;
    // 操作描述
    private String operMsg;
    // 请求URI
    private String reqUri;
    // 请求ip
    private String reqIp;
    // 操作方法
    private String operMethod;
    // 操作时间
    private String operTimes;
    // 请求参数
    private String reqParameter;
    // 返回结果
    private String respParameter;
    // 结束时间
    private String respTimes;
    // 耗时
    private String takeUpTime;


    // 日志类型1登入,2操作--
    private Integer logType;
    // 日志状态1正常2异常--
    private Integer logStatus;

}