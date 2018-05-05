package com.yiyi.farm.entity.invite;

import java.sql.Timestamp;

/**
 * @author peach
 * @date 2018-05-04 21:12:58
 * @description 代表用户功能的的数据实体接口
 */
public interface Customerable {
    String getPhone();

    Timestamp getTime();

    default boolean timeBetween(int start, int end){
        return (getTime().getNanos() >= start && getTime().getNanos() <= end);
    }
}
