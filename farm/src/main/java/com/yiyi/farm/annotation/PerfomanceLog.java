package com.yiyi.farm.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PerfomanceLog {
    String prefix()default "";
}
