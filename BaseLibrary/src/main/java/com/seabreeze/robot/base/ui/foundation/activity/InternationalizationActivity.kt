package com.seabreeze.robot.base.ui.foundation.activity

import android.content.Context
import android.os.Bundle
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.common.Settings

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/1
 * desc   : 主题、国际化
 * </pre>
 */
abstract class InternationalizationActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //主题适配
        setTheme(Settings.project_theme)
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        //语言国际化
        val context = Settings.language_status.let {
            LanguageHelper.switchLanguage(newBase, it, isForce = true)
        }
        super.attachBaseContext(context)
    }

}