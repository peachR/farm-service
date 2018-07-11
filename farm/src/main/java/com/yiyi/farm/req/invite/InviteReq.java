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
    private int startTimeByRecharge;    //充值信息专用的时间
    private int endTimeByRecharge;      //充值信息专用的时间
    private int rankNumber;

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

    public int getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(int rankNumber) {
        this.rankNumber = rankNumber;
    }

    public int getStartTimeByRecharge() {
        return startTimeByRecharge;
    }

    public void setStartTimeByRecharge(int startTimeByRecharge) {
        this.startTimeByRecharge = startTimeByRecharge;
    }

    public int getEndTimeByRecharge() {
        return endTimeByRecharge;
    }

    public void setEndTimeByRecharge(int endTimeByRecharge) {
        this.endTimeByRecharge = endTimeByRecharge;
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
