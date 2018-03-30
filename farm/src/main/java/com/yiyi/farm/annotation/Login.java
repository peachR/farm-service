package com.yiyi.farm.annotation;

import java.lang.annotation.*;

/**
 * @author peach
 * @date 2018-03-29 12:52:57
 * @description 用于控制器方法之上，表示该方法是否需要登录
 */
//只有方法可以使用该注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
    public boolean value() default true;
}
