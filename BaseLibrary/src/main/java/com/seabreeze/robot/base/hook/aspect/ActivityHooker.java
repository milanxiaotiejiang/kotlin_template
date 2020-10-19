//package com.seabreeze.robot.base.hook.aspect;
//
//import android.os.Bundle;
//
//import com.elvishew.xlog.XLog;
//import com.seabreeze.robot.base.hook.model.ActivityRecord;
//
//import org.aspectj.lang.annotation.Aspect;
//
//import me.ele.lancet.base.Origin;
//import me.ele.lancet.base.Scope;
//import me.ele.lancet.base.annotations.Insert;
//import me.ele.lancet.base.annotations.TargetClass;
//
///**
// * User: milan
// * Time: 2019/12/21 19:43
// * Des:
// */
//@Aspect
//public class ActivityHooker {
//
//    public static ActivityRecord sActivityRecord;
//
//    static {
//        sActivityRecord = new ActivityRecord();
//    }
//
//    @Insert(value = "onCreate", mayCreateSuper = true)
//    @TargetClass(value = "android.app.Activity", scope = Scope.ALL)
//    protected void onCreate(Bundle savedInstanceState) {
//        sActivityRecord.mOnCreateTime = System.currentTimeMillis();
//        Origin.callVoid();
//    }
//
//    @Insert(value = "onWindowFocusChanged", mayCreateSuper = true)
//    @TargetClass(value = "android.app.Activity", scope = Scope.ALL)
//    public void onWindowFocusChanged(boolean hasFocus) {
//        sActivityRecord.mOnWindowsFocusChangedTime = System.currentTimeMillis();
//        XLog.e("AOP onWindowFocusChanged cost " + (sActivityRecord.mOnWindowsFocusChangedTime - sActivityRecord.mOnCreateTime));
//        Origin.callVoid();
//    }
//
//    private static long runTime = 0;
//
//    @Insert(value = "run")
//    @TargetClass(value = "java.lang.Runnable", scope = Scope.ALL)
//    public void run() {
//        runTime = System.currentTimeMillis();
//        Origin.callVoid();
//        XLog.e("AOP runTime " + (System.currentTimeMillis() - runTime));
//    }
//
//}
