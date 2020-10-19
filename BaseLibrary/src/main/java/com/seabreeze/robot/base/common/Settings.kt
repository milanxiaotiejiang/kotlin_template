package com.seabreeze.robot.base.common

import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.LanguageHelper.LANGUAGE_SYSTEM
import com.seabreeze.robot.base.ext.Mmkv

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object Settings {
    var language_status: String by Mmkv("LANGUAGE_STATUS", LANGUAGE_SYSTEM)
    var j_push_state: Boolean by Mmkv("J_PUSH_STATE", true)
    var project_theme: Int by Mmkv("PROJECT_THEME", R.style.AppBaseTheme)
}