package com.seabreeze.robot.base.ext.tool

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * @description : TODO
 * </pre>
 */

fun AppCompatActivity.getStatusBarHeight(): Int {
    try {
        val c = Class.forName("com.android.internal.R\$dimen")
        val obj = c.newInstance()
        val field = c.getField("status_bar_height")
        val x = field[obj].toString().toInt()
        return resources.getDimensionPixelSize(x)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}


fun Fragment.getStatusBarHeight(): Int {
    try {
        val c = Class.forName("com.android.internal.R\$dimen")
        val obj = c.newInstance()
        val field = c.getField("status_bar_height")
        val x = field[obj].toString().toInt()
        return resources.getDimensionPixelSize(x)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

