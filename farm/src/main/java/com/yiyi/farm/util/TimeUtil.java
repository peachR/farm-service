package com.yiyi.farm.util;

import java.sql.Timestamp;

/**
 * @author peach
 * @date 2018-03-30 15:49:09
 * @description 时间处理
 */
public final class TimeUtil {
    //工具类,防止实例化
    private TimeUtil(){}

    public static Timestamp getTimestamp(long millisecond){
        return new Timestamp(millisecond);
    }
}
