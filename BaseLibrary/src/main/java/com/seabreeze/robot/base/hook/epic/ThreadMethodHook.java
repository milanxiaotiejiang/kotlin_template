package com.seabreeze.robot.base.hook.epic;

import com.elvishew.xlog.XLog;
import com.taobao.android.dexposed.XC_MethodHook;

/**
 * User: milan
 * Time: 2020/7/21 20:48
 * Des:
 */
public class ThreadMethodHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        XLog.i("thread:" + t + ", started..");
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        XLog.i("thread:" + t + ", exit..");
    }
}
