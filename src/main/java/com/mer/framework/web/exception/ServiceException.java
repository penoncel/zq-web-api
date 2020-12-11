package com.mer.framework.web.exception;

import com.mer.common.enums.ErrorStateEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义 异常处理
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    private Integer code;
    private String message;

    public ServiceException() {
    }

    public ServiceException(String message) {
        this.code = ErrorStateEnum.SYSTEM_ERROR.getCode();
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
