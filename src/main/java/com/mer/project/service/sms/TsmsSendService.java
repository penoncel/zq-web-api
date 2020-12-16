package com.mer.project.service.sms;

import com.mer.common.utils.ComUtils;
import com.mer.framework.config.redis.redisservice.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Program: zq-web-api
 * @Description: 发送短信
 * @Author: 赵旗
 * @Create: 2020-12-09 16:46
 */
@Slf4j
public class TsmsSendService implements Runnable{
    @Autowired
    RedisService redisService;
    private SmsSandSendPo smsSandSendPo;

    public TsmsSendService(SmsSandSendPo smsSandSendPo){
        this.smsSandSendPo = smsSandSendPo;
    }
    @Override
    public void run() {
        // 这里使用默认值，随机验证码的方法为CommonsUtils.getCode()
        int code = 7777;
        // todo 此处为发送验证码代码

        // 将验证码加密后存储到redis中
        redisService.set(smsSandSendPo.getPrefix(), smsSandSendPo.getPhone(), ComUtils.encryptPassword(String.valueOf(code)));
        Integer sendCount = 0;
        while (sendCount < 3) {
            ++sendCount;

            /**
             * 如果成功 则 break;
             */
        }
    }
}
