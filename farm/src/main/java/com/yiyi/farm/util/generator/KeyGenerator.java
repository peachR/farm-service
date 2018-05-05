package com.yiyi.farm.util.generator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peach
 * @date 2018-05-04 15:12:11
 * @description redis主键生成器
 */
public interface KeyGenerator {
    String getKey(String phone);
    String getPrefix();

}
