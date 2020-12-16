package com.mer.framework.web.domain;


import com.alibaba.fastjson.JSONObject;
import com.mer.common.enums.SysMsgEnum;

import java.util.HashMap;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
public class Result extends HashMap<Object, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private String CODE_TAG = "code";

    /**
     * 状态描述
     */
    private String MSG_TAG = "msg";

    /**
     * 反回数据
     */
    private Object DATA_TAG = "data";


    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  状态描述
     * @param data 反回数据
     */
    public Result(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }


    /**
     * 返回成功消息 - 直接信息提示
     *
     * @return 成功消息
     */
    public static Result success() {
        return new Result(SysMsgEnum.SUCCESS.getCode(), SysMsgEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 返回成功消息 - 带反回数据
     *
     * @return 成功消息
     */
    public static Result success(Object data) {
        return new Result(SysMsgEnum.SUCCESS.getCode(), SysMsgEnum.SUCCESS.getMsg(), data);
    }


    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static Result error() {
        return new Result(SysMsgEnum.ERROR.getCode(), SysMsgEnum.ERROR.getMsg(), null);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  状态描述
     * @return 警告消息
     */
    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }


    /**
     * 返回错误消息
     *
     * @param sysMsgEnum 错误信息枚举类
     * @return 警告消息
     */
    public static Result error(SysMsgEnum sysMsgEnum) {
        return new Result(sysMsgEnum.getCode(), sysMsgEnum.getMsg(), null);
    }


    /**
     * 反回String
     */
    public static String tJson(SysMsgEnum sysMsgEnum) {
        return JSONObject.toJSON(new Result(sysMsgEnum.getCode(), sysMsgEnum.getMsg(), null)).toString();
    }

}
