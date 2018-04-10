package com.yiyi.farm.entity.invite;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class InviteInfoEntity implements Serializable {
    private Integer id;

    private String uid;

    private String phone;

    private Integer playerId;

    private Integer serverId;

    private String upPlayerUid;

    private String upPlayerPhone;

    private Timestamp time;

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
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InviteInfoEntity that = (InviteInfoEntity) o;
        return Objects.equals(uid, that.uid) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(playerId, that.playerId) &&
                Objects.equals(serverId, that.serverId) &&
                Objects.equals(upPlayerUid, that.upPlayerUid) &&
                Objects.equals(upPlayerPhone, that.upPlayerPhone);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid, phone, playerId, serverId, upPlayerUid, upPlayerPhone);
    }

    @Override
    public String toString() {
        return "InviteInfoEntity{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", playerId=" + playerId +
                ", serverId=" + serverId +
                ", upPlayerUid='" + upPlayerUid + '\'' +
                ", upPlayerPhone='" + upPlayerPhone + '\'' +
                ", time=" + time +
                '}';
    }
}