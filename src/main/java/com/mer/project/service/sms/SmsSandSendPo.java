package com.mer.project.service.sms;

import lombok.Data;

/**
 * @Program: zq-web-api
 * @Description: 短信实体
 * @Author: 赵旗
 * @Create: 2020-12-09 16:48
 */
@Data
public class SmsSandSendPo {
    /** 手机号 **/
    private String mobile;

    /** 消息内容 **/
    private String message;

    public SmsSandSendPo(String mobile, String message) {
        this.mobile = mobile;
        this.message = message;
    }
}
