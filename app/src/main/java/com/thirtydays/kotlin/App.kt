package com.thirtydays.kotlin

import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.elvishew.xlog.XLog
import com.seabreeze.robot.data.DataApplication
import com.taobao.android.dexposed.DexposedBridge
import com.taobao.android.dexposed.XC_MethodHook
import io.reactivex.plugins.RxJavaPlugins

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/6
 * @description :
 * 1、使用时可将 fragmentation 等两个库去掉
 * 2、可将 BaseLibrary 中 hook 下剔除
</pre> *
 */
class App : DataApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            initWebViewDataDirectory(this)
        } else {
            initHookThread()
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            initHookImageView()
        }

//        initActivityLifecycleCallbacks()

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            XLog.e(
                """
                        ========================================================================================================
                        $throwable  
                        ${throwable.cause}${throwable.message}
                        ========================================================================================================
                    """
            )
        }

//        SoUtils.initAssetsFile(this)
    }


    private fun initHookThread() {
        DexposedBridge.hookAllConstructors(Thread::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                val thread = param.thisObject as Thread
//                XLog.i(thread.name + " stack " + Log.getStackTraceString(Throwable()))
            }
        })
    }

    private fun initHookImageView() {
        DexposedBridge.hookAllConstructors(ImageView::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
//                DexposedBridge.findAndHookMethod(ImageView::class.java, "setImageBitmap", Bitmap::class.java, ImageHook())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initWebViewDataDirectory(application: Application) {
        val processName = getProcessName()
        processName?.let {
            if (packageName != it) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }

}