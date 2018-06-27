package com.yiyi.farm.entity.invite;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018-6-27.
 */
public class LogRechargeEntity implements Serializable,Customerable {
    private int id;
    private String uid;
    private String phone;
    private int money;
    private int recharge;
    private Timestamp time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRecharge() {
        return recharge;
    }

    public void setRecharge(int recharge) {
        this.recharge = recharge;
    }

    @Override
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LogRechargeEntity{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", money=" + money +
                ", recharge=" + recharge +
                ", time=" + time +
                '}';
    }
}
