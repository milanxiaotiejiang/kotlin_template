@file:JvmName("KeyboardUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 软键盘工具类
 * </pre>
 */
/**
 * 打开软键盘
 */
fun View.showKeyboard(): Boolean {
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    return imm.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

/**
 * 关闭软键盘
 */
fun View.hideKeyboard(): Boolean {
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

/**
 * 根据当前软键盘的状态做取反操作
 */
fun View.toggleKeyboard() {
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}