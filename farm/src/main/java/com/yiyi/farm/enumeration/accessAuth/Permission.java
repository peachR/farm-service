package com.yiyi.farm.enumeration.accessAuth;

import com.yiyi.farm.enumeration.BaseEnum;

public enum Permission implements BaseEnum{
    ROOT("1", "root");

    private String code;
    private String msg;

    Permission(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMsg() {
        return null;
    }

}
