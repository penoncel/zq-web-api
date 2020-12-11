package com.mer.framework.web.exception;


import com.aliyuncs.exceptions.ClientException;

import com.mer.common.enums.ErrorStateEnum;
import com.mer.framework.web.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;


/**
 * 全局异常处理
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    /**
     * serviceException
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e) {
        return Result.error(e.getCode(),e.getMessage());
    }


    /**
     * 参数校验异常
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        return Result.error(400,e.getMessage());
    }


    /**
     * 阿里短信发送异常
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ClientException.class)
    public Result handleClientException(){
        return Result.error(ErrorStateEnum.SEND_SMS_ERROR.getCode(), ErrorStateEnum.SEND_SMS_ERROR.getMsg());
    }


    /**
     * shiro权限异常处理
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public Result handleShiroException() {
        return Result.error(ErrorStateEnum.NOT_AUTH.getCode(), ErrorStateEnum.NOT_AUTH.getMsg());
    }


    /**
     * token无效异常
     */
    @ResponseBody
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Result handleTokenException(){
        return Result.error(ErrorStateEnum.TOKEN_INVALID.getCode(), ErrorStateEnum.TOKEN_INVALID.getMsg());
    }


    /**
     * 参数校验(缺少)异常处理
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParameterException(){
        return Result.error(ErrorStateEnum.MISSING_PARAMETER.getCode(), ErrorStateEnum.MISSING_PARAMETER.getMsg());
    }



    /**
     * SYSTEM_ERROR
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e){
        log.error(e.getMessage());
        return Result.error(ErrorStateEnum.SYSTEM_ERROR.getCode(), ErrorStateEnum.SYSTEM_ERROR.getMsg());
    }

}
