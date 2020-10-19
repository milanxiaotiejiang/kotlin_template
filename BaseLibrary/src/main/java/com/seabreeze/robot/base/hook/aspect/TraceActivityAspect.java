package com.seabreeze.robot.base.hook.aspect;

import android.app.Activity;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 检测activity生命周期耗时
 * Kotlin 无效 需要重写出来 onXXX 才可以
 * 记录 Activity 的 on  开头的方法所用耗时
 */
@Aspect
public class TraceActivityAspect {

    @Pointcut("execution(* android.app.Activity.on**(..))")
    public void activityOnXXX() {
    }

    @Around("activityOnXXX()")
    public Object activityOnXXXAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        try {
            Activity activity = (Activity) proceedingJoinPoint.getTarget();
            long startTime = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            String activityName = activity.getClass().getCanonicalName();

            Signature signature = proceedingJoinPoint.getSignature();
            String sign = "";
            String methodName = "";
            if (signature != null) {
                sign = signature.toString();
                methodName = signature.getName();
            }

            long duration = System.currentTimeMillis() - startTime;
            if (!TextUtils.isEmpty(activityName) && !TextUtils.isEmpty(sign) && sign
                    .contains(activityName)) {
                XLog.d(String.format("AOP Activity：%s, %s方法执行了，用时%d ms", activityName, methodName, duration));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
