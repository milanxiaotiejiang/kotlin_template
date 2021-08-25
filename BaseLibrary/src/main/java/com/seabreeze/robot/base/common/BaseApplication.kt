package com.seabreeze.robot.base.common

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.seabreeze.robot.base.common.Settings.language_status
import com.seabreeze.robot.base.ext.initWebViewDataDirectory
import com.seabreeze.robot.base.hook.epic.initHookImageView
import com.seabreeze.robot.base.hook.epic.initHookThread
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig

/**
 * User: milan
 * Time: 2020/4/8 9:40
 * Des:
 */
private lateinit var INSTANCE: Application

open class BaseApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)

        initWebViewDataDirectory()
        //hook thread
        initHookThread()
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        //XLog
        val config = LogConfiguration.Builder()
            .tag("Base_XLog")
            .build()
        XLog.init(config, AndroidPrinter())

        //Logger
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("Base_Logger")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        val rootDir: String = MMKV.initialize(this)
        XLog.i(rootDir)

        //ARouter初始化
        ARouter.openLog() // 打印日志
        ARouter.openDebug()
        ARouter.init(this)

        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance()

        //腾讯
        CrashReport.initCrashReport(this, "d40d0616af", false)
//        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG)

        language_status.let {
            LanguageHelper.switchLanguage(this, it, isForce = true)
        }

        //hook image
        initHookImageView()

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }

}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}