package com.yiyi.farm.req.information;

import com.yiyi.farm.req.AbstractReq;
import java.sql.Timestamp;

/**
 * @author peach
 * @date 2018-03-30 14:35:03
 * @description 用户信息请求参数
 */
public class InformationReq extends AbstractReq {
    /** 客户手机号 */
    private String[] phone;

    /** 客户账号 */
    private String number;

    /** 起始时间 */
    private Timestamp begDate;

    /** 结束时间 */
    private Timestamp endDate;

    /** 页数 */
    private int page;

    /** 每页行数 */
    private int items;

    public String[] getPhone() {
        return phone;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Timestamp getBegDate() {
        return begDate;
    }

    public void setBegDate(Timestamp begDate) {
        this.begDate = begDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
