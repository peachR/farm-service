package com.yiyi.farm.entity.invite;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogConsumeEntity implements Serializable, Customerable {
    private int id;
    private String uid;
    private String phone;
    private int type;
    private String info;
    private int money;
    private int hongbao;
    private int yuer;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHongbao() {
        return hongbao;
    }

    public void setHongbao(int hongbao) {
        this.hongbao = hongbao;
    }

    public int getYuer() {
        return yuer;
    }

    public void setYuer(int yuer) {
        this.yuer = yuer;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LogConsume{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                ", info='" + info + '\'' +
                ", money=" + money +
                ", hongbao=" + hongbao +
                ", yuer=" + yuer +
                ", time=" + time +
                '}';
    }
}
