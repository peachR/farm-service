package com.yiyi.farm.util;

import com.yiyi.farm.enumeration.BaseEnum;

/**
 * @author peach
 * @date 2018-03-30 14:16:21
 * @description
 */
public class EnumUtil {
    /**
     * 根据Code获取枚举
     * @param type
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends Enum<?> & BaseEnum> E codeOf(Class<E> type, String code){
        E[] enumConstants = type.getEnumConstants();
        for(E e : enumConstants){
            if(code.equals(e.getCode())){
                return e;
            }
        }
        return null;
    }

    /**
     * 根据message获取枚举
     * @param type
     * @param msg
     * @param <E>
     * @return
     */
    public static <E extends Enum<?> & BaseEnum> E msgOf(Class<E> type, String msg){
        E[] enumConstants = type.getEnumConstants();
        for(E e: enumConstants){
            if(msg.equals(e.getMsg())){
                return e;
            }
        }
        return null;
    }
}
