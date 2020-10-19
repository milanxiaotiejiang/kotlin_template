package com.seabreeze.robot.base.ui.activity

import android.content.Context
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.common.Settings

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/1
 * desc   :
 * </pre>
 */
abstract class InternationalizationActivity : BaseActivity() {

    override fun attachBaseContext(newBase: Context) {
        val context = Settings.language_status.let {
            LanguageHelper.switchLanguage(newBase, it, isForce = true)
        }
        super.attachBaseContext(context)
    }
}