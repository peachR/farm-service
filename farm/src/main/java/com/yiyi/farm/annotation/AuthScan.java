package com.yiyi.farm.annotation;

import java.lang.annotation.*;

/**
 * @author peach
 * @date 2018-04-25 11:08:31
 * @description 指定要扫描权限的包
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthScan {
    public String value();
}
