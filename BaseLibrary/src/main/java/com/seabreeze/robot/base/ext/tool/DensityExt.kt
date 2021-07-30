@file:JvmName("DensityUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.res.Resources

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 单位转换工具类
 * </pre>
 */
fun dp2px(dpValue: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5F)
}

fun dp2px(dpValue: Int) = dp2px(dpValue.toFloat()).toInt()

fun px2dp(pxValue: Float): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale + 0.5F)
}

fun px2dp(pxValue: Int) = px2dp(pxValue.toFloat()).toInt()

fun sp2px(spValue: Float): Float {
    val fontScale = Resources.getSystem().displayMetrics.density
    return (spValue * fontScale + 0.5F)
}

fun sp2px(spValue: Int) = sp2px(spValue.toFloat()).toInt()

fun px2sp(pxValue: Float): Float {
    val fontScale = Resources.getSystem().displayMetrics.density
    return (pxValue / fontScale + 0.5F)
}

fun px2sp(pxValue: Int) = px2sp(pxValue.toFloat()).toInt()