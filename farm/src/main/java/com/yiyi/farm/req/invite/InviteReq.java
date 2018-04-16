package com.yiyi.farm.req.invite;

import com.yiyi.farm.req.AbstractReq;

public class InviteReq extends AbstractReq {
    private String[] phone;
    private String[] uid;
    private int totalConsume;
    private int chargeConsume;


    public String[] getPhone() {
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

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String[] getUid() {
        return uid;
    }

    public void setUid(String[] uid) {
        this.uid = uid;
    }
}
