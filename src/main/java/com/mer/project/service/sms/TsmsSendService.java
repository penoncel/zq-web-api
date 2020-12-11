package com.mer.project.service.sms;

import lombok.extern.slf4j.Slf4j;

/**
 * @Program: zq-web-api
 * @Description: 发送短信
 * @Author: 赵旗
 * @Create: 2020-12-09 16:46
 */
@Slf4j
public class TsmsSendService implements Runnable{

    private SmsSandSendPo smsSandSendPo;

    public TsmsSendService(SmsSandSendPo smsSandSendPo){
        this.smsSandSendPo = smsSandSendPo;
    }
    @Override
    public void run() {
        Integer sendCount = 0;
        while (sendCount < 3) {
            ++sendCount;

            /**
             * 如果成功 则 break;
             */
        }
    }
}
