package com.yiyi.farm.rsp;

import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;

import java.io.Serializable;

/**
 * @author peach
 * @date 2018-03-30 10:33:50
 * @description 统一响应结果
 * @param <T>
 */
public class Result<T> implements Serializable {
    private boolean isSuccess;//请求成功处理

    private String errorMsg;//错误消息

    private String errorCode;//错误代码

    private T data;//返回数据

    public static <T> Result<T> newSuccessResult(){
        Result<T> result = new Result<>();
        result.isSuccess = true;
        return result;
    }

    public static<T> Result<T> newSuccessResult(T data){
        Result<T> result = new Result<>();
        result.isSuccess = true;
        result.data = data;
        return result;
    }

    public static <T> Result<T> newFailureResult(){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorCode = ExpCodeEnum.UNKNOW_ERROR.getCode();
        result.errorMsg = ExpCodeEnum.UNKNOW_ERROR.getMsg();
        return result;
    }

    public static <T> Result<T> newFailureResult(CommonException exp){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorMsg = exp.getCodeEnum().getMsg();
        result.errorCode = exp.getCodeEnum().getCode();
        return result;
    }

    public static <T> Result<T> newFailureResult(CommonException exp, T data){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorMsg = exp.getCodeEnum().getMsg();
        result.errorCode = exp.getCodeEnum().getCode();
        result.data = data;
        return result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString(){
        return "Result{" +
                "isSuccess=" + isSuccess +
                ", errorCode=" + errorCode +
                ", message='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
