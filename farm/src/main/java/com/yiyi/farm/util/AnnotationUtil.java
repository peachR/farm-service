package com.yiyi.farm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author peach
 * @date 2018-04-10 15:05:39
 * @descripted 注解工具类
 */
public class AnnotationUtil {
    /**
     * 获取类上面的指定注解
     * @param clazz 指定类
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T> T getAnnotationValueByClass(Class clazz, Class<T> annotationClazz){
        return (T)clazz.getAnnotation(annotationClazz);
    }

    /**
     * 获取方法上面的指定注解
     * @param method 指定方法
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T> T getAnnotationValueByMethod(Method method, Class annotationClazz){
        return (T)method.getAnnotation(annotationClazz);
    }

    /**
     * 获取域上面的指定注解
     * @param field 指定域
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T> T getAnnotationValueByField(Field field, Class annotationClazz){
        return (T)field.getAnnotation(annotationClazz);
    }
}
