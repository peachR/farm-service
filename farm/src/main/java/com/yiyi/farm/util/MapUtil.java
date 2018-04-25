package com.yiyi.farm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peach
 * @date 2018-04-25 13:39:43
 * @description map工具类
 */
public final class MapUtil {
    private MapUtil(){}

    public static <K,V> Map<K,V> newHashMap(){
        return new HashMap<>();
    }

    public static <K,V> Map<K,V> newHashMapWithCapacity(int capacity){
        return new HashMap<>(capacity);
    }

    public static <K,V> Map<K,V> newHashMap(Map<K,V> map){
        return new HashMap<>(map);
    }
}
