package com.mer.common.redis.key;

import com.mer.common.constant.Constant;
import com.mer.framework.config.redis.keyset.BasePrefix;

/**
 * @Program: zq-web-api
 * @Description: 防刷 key
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class AppConnectKey extends BasePrefix {

    private AppConnectKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    /**
     * 用户信息 token
     */
    public static AppConnectKey requestLimit = new AppConnectKey(Constant.REPFAT_TIMEOUT,"brush:");


}
