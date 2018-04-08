package com.yiyi.farm.entity.invite;

import java.io.Serializable;
import java.sql.Timestamp;

public class InviteRelationEntity implements Serializable {
    private Integer id;

    private String uid;

    private String phone;

    private Integer playerId;

    private Integer serverId;

    private String upPlayerUid;

    private String upPlayerPhone;

    private Timestamp time;

    private String childPlayerUid;

    private String childPlayerPhone;

    private Integer high;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getUpPlayerUid() {
        return upPlayerUid;
    }

    public void setUpPlayerUid(String upPlayerUid) {
        this.upPlayerUid = upPlayerUid == null ? null : upPlayerUid.trim();
    }

    public String getUpPlayerPhone() {
        return upPlayerPhone;
    }

    public void setUpPlayerPhone(String upPlayerPhone) {
        this.upPlayerPhone = upPlayerPhone == null ? null : upPlayerPhone.trim();
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getChildPlayerUid() {
        return childPlayerUid;
    }

    public void setChildPlayerUid(String childPlayerUid) {
        this.childPlayerUid = childPlayerUid == null ? null : childPlayerUid.trim();
    }

    public String getChildPlayerPhone() {
        return childPlayerPhone;
    }

    public void setChildPlayerPhone(String childPlayerPhone) {
        this.childPlayerPhone = childPlayerPhone == null ? null : childPlayerPhone.trim();
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "InviteRelationEntity{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", playerId=" + playerId +
                ", serverId=" + serverId +
                ", upPlayerUid='" + upPlayerUid + '\'' +
                ", upPlayerPhone='" + upPlayerPhone + '\'' +
                ", time=" + time +
                ", childPlayerUid='" + childPlayerUid + '\'' +
                ", childPlayerPhone='" + childPlayerPhone + '\'' +
                ", high=" + high +
                '}';
    }
}