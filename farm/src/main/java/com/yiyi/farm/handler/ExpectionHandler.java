package com.yiyi.farm.handler;

import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;
import com.yiyi.farm.rsp.Result;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author peach
 * @date 2018-03-29 18:16:07
 * @description 异常统一拦截处理
 */
@ControllerAdvice
@ResponseBody
public class ExpectionHandler {
    /**
     * 业务异常处理
     * @param exp
     * @param <T>
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public <T> Result<T> exceptionHandler(CommonException exp){
        return Result.newFailureResult(exp);
    }

    /**
     * 请求方法错误异常
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        throw new CommonException(ExpCodeEnum.HTTP_REQ_METHOD_ERROR);
    }

    /**
     * 处理系统异常
     * @param exp
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result sysException(Exception exp){
        exp.printStackTrace();
        return Result.newFailureResult();
    }
}
