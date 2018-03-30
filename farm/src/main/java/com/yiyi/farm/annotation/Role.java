package com.yiyi.farm.annotation;

import java.lang.annotation.*;

/**
 * @author peach
 * @date 2018-03-29 12:55:26
 * @description 用于控制器方法上，表名可以使用该方法的角色
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Role {
    public String value();
}
