package com.mer.project.service.sms;

import com.mer.framework.config.redis.keyset.KeyPrefix;
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
    private String phone;
    /** 消息内容 **/
    private String message;
    /** redis key **/
    private KeyPrefix prefix;

    public SmsSandSendPo(KeyPrefix prefix,String phone, String message) {
        this.prefix = prefix;
        this.phone = phone;
        this.message = message;
    }
}
