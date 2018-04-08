package com.yiyi.farm.req.invite;

import com.yiyi.farm.req.AbstractReq;

public class InviteReq extends AbstractReq {
    private String[] phone;
    private String[] uid;


    public String[] getPhone() {
        return phone;
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
