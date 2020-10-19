package com.seabreeze.robot.base.hook.epic

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebView
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.elvishew.xlog.XLog
import com.taobao.android.dexposed.DexposedBridge
import com.taobao.android.dexposed.XC_MethodHook

/**
 * User: milan
 * Time: 2020/7/21 21:19
 * Des:
 */

fun Application.initHookImageView() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
        DexposedBridge.hookAllConstructors(ImageView::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                DexposedBridge.findAndHookMethod(
                    ImageView::class.java,
                    "setImageBitmap",
                    Bitmap::class.java,
                    ImageHook()
                )
            }
        })
    }
}

fun Application.initHookThread() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
        DexposedBridge.hookAllConstructors(Thread::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                val thread = param.thisObject as Thread
                val clazz: Class<*> = thread.javaClass
                if (clazz != Thread::class.java) {
                    XLog.d("found class extend Thread:$clazz")
                    DexposedBridge.findAndHookMethod(clazz, "run", ThreadMethodHook())
                }
                XLog.d("Thread: " + thread.name + " class:" + thread.javaClass + " is created.")
            }
        })
        DexposedBridge.findAndHookMethod(Thread::class.java, "run", ThreadMethodHook())
    }
}