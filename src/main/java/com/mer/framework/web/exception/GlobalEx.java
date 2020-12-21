package com.mer.framework.web.exception;


import com.mer.common.enums.SysMsgEnum;
import com.mer.common.utils.ComUtils;
import com.mer.framework.web.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
    private static String WARN = "WARN";
    private static String ERROR = "ERROR";

    /**
     * serviceException 内部自定义异常
     *
     * @param e e
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(ServiceEx.class)
    public Result handleServiceException(ServiceEx e) {
        logMsg("自定义异常", WARN, e.getMessage());
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
        logMsg("权限不足", WARN, Result.tJson(SysMsgEnum.NOT_AUTH));
        return Result.error(SysMsgEnum.NOT_AUTH.getCode(), SysMsgEnum.NOT_AUTH.getMsg());
    }


    /**
     * token无效异常
     */
    @ResponseBody
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Result handleTokenException(IncorrectCredentialsException e) {
        logMsg("token已过期", WARN, e.getMessage());
        return Result.error(SysMsgEnum.TOKEN_INVALID.getCode(), SysMsgEnum.TOKEN_INVALID.getMsg());
    }


    /**
     * 参数缺失
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParameterException(MissingServletRequestParameterException e) {
        logMsg("参数缺失", WARN,
                String.format(
                        "参数:[{%s}],类型:[{%s}],描述:[{%s}]", e.getParameterName(), e.getParameterType(), e.getMessage()
                )
        );
        return Result.error(SysMsgEnum.MISSING_PARAMETER.getCode(), SysMsgEnum.MISSING_PARAMETER.getMsg() + "[" + e.getParameterName() + "]");
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
        logMsg("参数校验错误", WARN, e.getMessage());
        return Result.error(SysMsgEnum.ERROR.getCode(), "参数校验错误：" + e.getMessage());
    }


    /**
     * 服务器内部出现错误
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        logMsg("系统异常", ERROR, e.getMessage());
        return Result.error(SysMsgEnum.INTERNAL_SERVER_ERROR.getCode(), SysMsgEnum.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 记录日志
     *
     * @param exName 异常名
     * @param type   类型(error warn)
     * @param msg
     */
    private void logMsg(String exName, String type, String msg) {
        Object user = SecurityUtils.getSubject().getPrincipal();
        if (WARN.equals(type)) {
            log.warn(exName + " ==> phone:[{}],uri:[{}],msg:[{}]", user != null ? user : "尚未登入", ComUtils.getRequest().getRequestURI(), msg);
        } else {
            log.error(exName + " ==> phone:[{}],uri:[{}],msg:[{}]", user != null ? user : "尚未登入", ComUtils.getRequest().getRequestURI(), msg);
        }
    }
}
