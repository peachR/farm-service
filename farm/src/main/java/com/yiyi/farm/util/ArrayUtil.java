package com.yiyi.farm.util;

import java.lang.reflect.Array;

public final class ArrayUtil {
    //工具类，禁止实例化
    private ArrayUtil(){}

    public static<T> boolean isEmpty(T[] arr){
        return !(arr != null && arr.length > 0);
    }

}
