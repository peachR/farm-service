package com.yiyi.farm.util.generator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peach
 * @date 2018-05-04 15:12:11
 * @description redis主键生成器
 */
public abstract class KeyGenerator {
    public String getKey(String str){
        return getPrefix() + str;
    }
    public abstract String getPrefix();

}
