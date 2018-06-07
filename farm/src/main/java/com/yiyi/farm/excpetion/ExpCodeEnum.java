package com.yiyi.farm.excpetion;

import com.yiyi.farm.util.ExpPrefixUtil;

import java.io.Serializable;

/**
 * @author peach
 * @date 2018-03-30 10:32:56
 * @description 异常代码及说明枚举
 */
public enum ExpCodeEnum implements Serializable{

    UNKNOW_ERROR(ExpPrefixUtil.COMEXPPREFIX + "000","未知异常"),
    HTTP_REQ_METHOD_ERROR(ExpPrefixUtil.COMEXPPREFIX + "005", "HTTP请求方法不正确"),
    UNLOGIN(ExpPrefixUtil.COMEXPPREFIX + "002", "未登录"),
    NO_PERMISSION(ExpPrefixUtil.CUSTOMEREXPPREFIX + "003", "权限不足"),

    INFORMATIONREQ_NULL(ExpPrefixUtil.REQEXPPREFIX + "001", "InformationReq为空");


    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ExpCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
