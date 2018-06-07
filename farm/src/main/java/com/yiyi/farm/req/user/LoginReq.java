package com.yiyi.farm.req.user;

import com.yiyi.farm.req.AbstractReq;

import java.util.Objects;

/**
 * 用户登录请求
 */
public class LoginReq extends AbstractReq {
    private String number;
    private String password;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginReq{" +
                "number='" + number + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginReq loginReq = (LoginReq) o;
        return Objects.equals(number, loginReq.number) &&
                Objects.equals(password, loginReq.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(number, password);
    }
}
