package com.yiyi.farm.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public final class AopUtil {
    private AopUtil(){}

    public static String getMethodName(ProceedingJoinPoint joinPoint) {
        return getMethod(joinPoint).getName();
    }

    public static Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if(signature instanceof MethodSignature){
            MethodSignature methodSignature = (MethodSignature)signature;
            return methodSignature.getMethod();
        }
        throw new RuntimeException("can not find method");
    }

    public static Class<?> getClazz(ProceedingJoinPoint joinPoint){
        return joinPoint.getTarget().getClass();
    }

    public static Class<?>[] getArgs(ProceedingJoinPoint joinPoint){
        return getMethod(joinPoint).getParameterTypes();
    }
}
