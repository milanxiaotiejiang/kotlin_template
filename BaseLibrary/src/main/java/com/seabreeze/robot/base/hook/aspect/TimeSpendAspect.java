package com.seabreeze.robot.base.hook.aspect;

import com.elvishew.xlog.XLog;
import com.seabreeze.robot.base.hook.annotation.TimeSpend;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * User: milan
 * Time: 2019/11/19 16:38
 * Des: 例子 ： @TimeSpend("测试时间")
 * 测试方法所用耗时
 */
@Aspect
public class TimeSpendAspect {

    @Pointcut("execution(@com.seabreeze.robot.base.hook.annotation.TimeSpend * *(..))")
    public void methodTime() {
    }

    @Around("methodTime()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String funName = methodSignature.getMethod().getAnnotation(TimeSpend.class).value();
        //统计时间
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - begin;
        XLog.d(String.format("AOP 功能：%s,%s类的%s方法执行了，用时%d ms", funName, className, methodName, duration));
        return result;
    }
}
