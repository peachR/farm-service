package com.yiyi.farm.util;

import java.util.Collection;

/**
 * @author  peach
 * @date 2018-05-04 21:54:13
 * @description 集合工具类
 */
public class CollectionUtil {
    private CollectionUtil(){}

    public static <E> boolean isEmpty(Collection<E> collection){
        return collection == null || collection.isEmpty();
    }
}
