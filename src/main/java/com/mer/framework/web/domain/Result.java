package com.mer.framework.web.domain;


import com.mer.common.enums.ErrorStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@ApiModel(value="Result", description="反回结果")
public class Result extends HashMap<Object, Object> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码")
    private String CODE_TAG = "code";


    @ApiModelProperty(value = "状态描述")
    private String MSG_TAG = "msg";


    @ApiModelProperty(value = "反回数据")
    private Object DATA_TAG = "data";


    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public Result(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Result(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * @return 成功消息
     */
    public static Result success() {
        return Result.success("操作成功");
    }

    /**
     * 返回成功数据
     * @return 成功消息
     */
    public static Result success(Object data) {
        return Result.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Result success(String msg) {
        return Result.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static Result success(String msg, Object data) {
        return new Result(HttpStatus.OK.value(), msg, data);
    }

    /**
     * 返回错误消息
     */
    public static Result error() {
        return Result.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Result error(String msg) {
        return Result.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Result error(String msg, Object data) {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param errorStateEnum  错误信息枚举类
     * @return 警告消息
     */
    public static Result error(ErrorStateEnum errorStateEnum) {
        return Result.error(errorStateEnum.getCode(), errorStateEnum.getMsg());
    }

    /**
     * 返回错误消息
     *
     * @param errorStateEnum  错误信息枚举类
     * @param data  返回内容
     * @return 警告消息
     */
    public static Result error(ErrorStateEnum errorStateEnum, Object data) {
        return new Result(errorStateEnum.getCode(), errorStateEnum.getMsg(), data);
    }
}
