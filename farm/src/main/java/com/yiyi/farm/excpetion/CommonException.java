package com.yiyi.farm.excpetion;

import java.io.Serializable;

/**
 * @author peach
 * @date 2018-03-30 10:32:12
 * @description 普通业务异常
 */
public class CommonException extends RuntimeException implements Serializable {
    public ExpCodeEnum getCodeEnum() {
        return codeEnum;
    }

    private ExpCodeEnum codeEnum;

    public CommonException(ExpCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }

    public CommonException(){}
}
