package com.mer.framework.web.exception;


import com.mer.common.enums.SysMsgEnum;
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
 *
 * @author zhaoqi
 * @date 2020/5/20 17:20
 */
@RestControllerAdvice
@Slf4j
public class GlobalEx {

    /**
     * serviceException 内部自定义异常
     *
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ServiceEx.class)
    public Result handleServiceException(ServiceEx e) {
        return Result.error(e.getCode(), e.getMessage());
    }





    /**
     * shiro权限异常处理
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public Result handleShiroException(AuthorizationException e) {
        log.warn("权限不足："+Result.tJson(SysMsgEnum.NOT_AUTH));
        return Result.error(SysMsgEnum.NOT_AUTH.getCode(), SysMsgEnum.NOT_AUTH.getMsg());
    }


    /**
     * token无效异常
     */
    @ResponseBody
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Result handleTokenException(IncorrectCredentialsException e) {
        return Result.error(SysMsgEnum.TOKEN_INVALID.getCode(), SysMsgEnum.TOKEN_INVALID.getMsg());
    }


    /**
     * 参数校验(缺少)异常处理
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParameterException(MissingServletRequestParameterException e) {
        log.error("参数校验异常:"+e.getMessage());
        return Result.error(SysMsgEnum.MISSING_PARAMETER.getCode(), "参数缺少,"+SysMsgEnum.MISSING_PARAMETER.getMsg());
    }


    /**
     * 参数校验异常
     *
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        return Result.error(SysMsgEnum.ERROR.getCode(), "参数校验错误："+e.getMessage());
    }


    /**
     * 服务器内部出现错误
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        log.error("异常信息"+e.getMessage());
        return Result.error(SysMsgEnum.INTERNAL_SERVER_ERROR.getCode(), SysMsgEnum.INTERNAL_SERVER_ERROR.getMsg());
    }

}
