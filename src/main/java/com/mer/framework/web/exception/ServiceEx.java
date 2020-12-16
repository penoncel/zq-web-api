package com.mer.framework.web.exception;

import com.mer.common.enums.SysMsgEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义 异常处理
 *
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceEx extends RuntimeException {

    private Integer code;
    private String message;

    public ServiceEx() {
    }

    public ServiceEx(String message) {
        this.code = SysMsgEnum.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }

    public ServiceEx(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceEx(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceEx(Throwable cause) {
        super(cause);
    }

}
