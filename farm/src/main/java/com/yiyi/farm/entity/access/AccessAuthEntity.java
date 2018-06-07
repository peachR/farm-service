package com.yiyi.farm.entity.access;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;

import java.util.Arrays;

public class AccessAuthEntity {
    private String url;
    private String methodName;
    private HttpMethodEnum httpMethodEnum;
    private boolean isLogin;
    private String[] permission;

    public AccessAuthEntity(){}

    public AccessAuthEntity(String url, String methodName, HttpMethodEnum httpMethodEnum, boolean isLogin, String[] permission) {
        this.url = url;
        this.methodName = methodName;
        this.httpMethodEnum = httpMethodEnum;
        this.isLogin = isLogin;
        this.permission = permission;
    }

    public static AccessAuthEntity ofNullable(String url, String methodName, HttpMethodEnum httpMethodEnum, boolean isLogin, String[] permission){
        if(url == null || httpMethodEnum == null)
            return null;
        return new AccessAuthEntity(url, methodName, httpMethodEnum, isLogin, permission);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public HttpMethodEnum getHttpMethodEnum() {
        return httpMethodEnum;
    }

    public void setHttpMethodEnum(HttpMethodEnum httpMethodEnum) {
        this.httpMethodEnum = httpMethodEnum;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "AccessAuthEntity{" +
                "url='" + url + '\'' +
                ", methodName='" + methodName + '\'' +
                ", httpMethodEnum=" + httpMethodEnum +
                ", isLogin=" + isLogin +
                ", permission=" + Arrays.toString(permission) +
                '}';
    }
}
