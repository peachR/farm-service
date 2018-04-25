package com.yiyi.farm.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author peach
 * @date 2018-04-10 15:05:39
 * @descripted 注解工具类
 */
public final class AnnotationUtil {
    //工具类,防止实例化
    private AnnotationUtil(){}
    /**
     * 获取类上面的指定注解
     * @param clazz 指定类
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T extends Annotation> Optional<T> getAnnotationValueByClass(Class clazz, Class<T> annotationClazz){
        return Optional.ofNullable((T)clazz.getAnnotation(annotationClazz));
    }

    /**
     * 获取方法上面的指定注解
     * @param method 指定方法
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T extends Annotation> Optional<T> getAnnotationValueByMethod(Method method, Class<T> annotationClazz){
        return Optional.ofNullable((T)method.getAnnotation(annotationClazz));
    }

    /**
     * 获取域上面的指定注解
     * @param field 指定域
     * @param annotationClazz 指定注解
     * @param <T> 注解的类型
     * @return
     */
    public static <T extends Annotation> Optional<T> getAnnotationValueByField(Field field, Class<T> annotationClazz){
        return Optional.ofNullable((T)field.getAnnotation(annotationClazz));
    }
}
