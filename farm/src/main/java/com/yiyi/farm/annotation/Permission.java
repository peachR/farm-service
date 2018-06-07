package com.yiyi.farm.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Login(value = true)
@Documented
public @interface Permission {
    public String[] value();
    com.yiyi.farm.enumeration.accessAuth.Permission[] permissions() default {};
}
