@file:JvmName("ResUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.seabreeze.robot.base.common.AppContext

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 资源相关工具类
 * </pre>
 */
/*
 * 获取主题属性id
 */
fun TypedValue.resourceId(resId: Int, theme: Resources.Theme): Int {
    theme.resolveAttribute(resId, this, true)
    return this.resourceId
}

/**
 * 获取颜色
 */
@JvmName("getColor")
fun getResColor(@ColorRes colorRes: Int) = ContextCompat.getColor(AppContext, colorRes)

/**
 * 获取图片资源
 */
@JvmName("getDrawable")
fun getResDrawable(@DrawableRes drawableRes: Int) =
    ContextCompat.getDrawable(AppContext, drawableRes)

/**
 * 获取字符资源
 */
@JvmName("getString")
fun getResString(@StringRes stringId: Int, vararg formatArgs: Any) =
    AppContext.getString(stringId, *formatArgs)

/**
 * 获取String数组
 */
@JvmName("getStringArray")
fun getResStringArray(@ArrayRes arrayId: Int): Array<String> =
    AppContext.resources.getStringArray(arrayId)

/**
 * 获取Int数组
 */
@JvmName("getIntArray")
fun getResIntArray(@ArrayRes arrayId: Int) = AppContext.resources.getIntArray(arrayId)

/**
 * 获取Char数组
 */
@JvmName("getTextArray")
fun getResTextArray(@ArrayRes arrayId: Int): Array<CharSequence> =
    AppContext.resources.getTextArray(arrayId)

/**
 * 获取dimens资源
 * 单位为px
 */
@JvmName("getDimenPx")
fun getResDimenPx(@DimenRes dimenRes: Int) =
    AppContext.resources.getDimensionPixelSize(dimenRes)

/**
 * 获取dimens中单位为dp的资源
 */
@JvmName("getDimenDp")
fun getResDimenDp(@DimenRes dimenRes: Int) =
    px2dp(AppContext.resources.getDimensionPixelSize(dimenRes))

/**
 * 获取dimens中单位为Sp的资源
 */
@JvmName("getDimenSp")
fun getResDimenSp(@DimenRes dimenRes: Int) =
    px2sp(AppContext.resources.getDimensionPixelSize(dimenRes))


fun ViewGroup.getItemView(@LayoutRes layoutResId: Int): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, false)
}