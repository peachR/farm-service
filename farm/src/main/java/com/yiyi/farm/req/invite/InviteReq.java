package com.yiyi.farm.req.invite;

import com.yiyi.farm.req.AbstractReq;

import java.sql.Timestamp;
import java.util.Arrays;

public class InviteReq extends AbstractReq {
    private String phone;
    private String[] uid;
    private int totalConsume;
    private int chargeConsume;
    private int startTime;
    private int endTime;

    public String getPhone() {
        return phone;
    }

    public int getChargeConsume() {
        return chargeConsume;
    }

    public void setChargeConsume(int chargeConsume) {
        this.chargeConsume = chargeConsume;
    }

    public int getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(int totalConsume) {
        this.totalConsume = totalConsume;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String[] getUid() {
        return uid;
    }

    public void setUid(String[] uid) {
        this.uid = uid;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "InviteReq{" +
                "phone='" + phone + '\'' +
                ", uid=" + Arrays.toString(uid) +
                ", totalConsume=" + totalConsume +
                ", chargeConsume=" + chargeConsume +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
