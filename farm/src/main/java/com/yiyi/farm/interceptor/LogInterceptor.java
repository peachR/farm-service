package com.yiyi.farm.interceptor;

import com.yiyi.farm.util.AopUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogInterceptor {

    private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Pointcut("@annotation(com.yiyi.farm.annotation.MethodLog)")
    public void hanleLog(){}

    @Around("hanleLog()")
    public Object log(ProceedingJoinPoint pj){
        try{
            Object result = pj.proceed();
            logger.info(buildMethodName(pj) + " return : " + result.toString());
            return result;
        }catch (Throwable e){
            logger.error("log error", e);
        }
        throw new RuntimeException("log error");
    }

    private String buildMethodName(ProceedingJoinPoint pj){
        String methodName = AopUtil.getMethodName(pj);
        String clazzName = AopUtil.getClazz(pj).getSimpleName();
        Class<?>[] args = AopUtil.getArgs(pj);
        return clazzName + "." + methodName + "(" + Arrays.toString(args) + ")";
    }
}
