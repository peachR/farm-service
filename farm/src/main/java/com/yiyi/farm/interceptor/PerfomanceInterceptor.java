package com.yiyi.farm.interceptor;

import com.yiyi.farm.annotation.PerfomanceLog;
import com.yiyi.farm.util.AopUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerfomanceInterceptor {
    private Logger logger = LoggerFactory.getLogger(PerfomanceLog.class);

    @Pointcut("@annotation(com.yiyi.farm.annotation.PerfomanceLog)")
    public void interceptPoint(){}

    @Around("interceptPoint()")
    public Object handlePerfomanceLog(ProceedingJoinPoint joinPoint){
        try{
            String methodName = AopUtil.getMethodName(joinPoint);
            logger.info(methodName + " begin");
            long start = System.nanoTime();
            Object result = joinPoint.proceed();
            logger.info(methodName + " end : " + (System.nanoTime() - start) / 1_000_000);
            return result;
        }catch (Throwable ex){
            logger.error(AopUtil.getMethodName(joinPoint) + " error ");
        }
        throw new RuntimeException("perfomance error");
    }

}
