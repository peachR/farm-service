package com.yiyi.farm.entity.customer;

import java.sql.Timestamp;

public class CustomerEntity {
    private Integer uid;    //用户id
    private Integer server_id;  //服务器id
    private String phone;   //手机号
    private Timestamp time;   //注册时间
    private String ip;  //注册ip
    private String name;    //昵称
    private Integer hongbao;    //红包
    private Integer yuer;   //余额
    private Boolean closed;    //封号
    private Boolean customer_account;   //客服账号

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHongbao() {
        return hongbao;
    }

    public void setHongbao(Integer hongbao) {
        this.hongbao = hongbao;
    }

    public Integer getYuer() {
        return yuer;
    }

    public void setYuer(Integer yuer) {
        this.yuer = yuer;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getCustomer_account() {
        return customer_account;
    }

    public void setCustomer_account(Boolean customer_account) {
        this.customer_account = customer_account;
    }
}
