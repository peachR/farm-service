package com.yiyi.farm.util;

import java.sql.Timestamp;

/**
 * @author peach
 * @date 2018-03-30 15:49:09
 * @description 时间处理
 */
public class TimeUtil {

    public static Timestamp timestampOf(long millisecond){
        return new Timestamp(millisecond);
    }
}
