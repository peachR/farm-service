package com.yiyi.farm.util;

import java.sql.Timestamp;

public class TimeUtil {

    public static Timestamp timestampOf(long millisecond){
        return new Timestamp(millisecond);
    }
}
