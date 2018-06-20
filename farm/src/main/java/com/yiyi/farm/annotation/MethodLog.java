package com.yiyi.farm.annotation;

import java.lang.annotation.*;

/**
 * @author peach
 * @date
 * @description 被该注解标识的方法将会被打上日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {
}
