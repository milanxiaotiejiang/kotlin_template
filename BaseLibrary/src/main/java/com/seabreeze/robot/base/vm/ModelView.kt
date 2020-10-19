package com.seabreeze.robot.base.vm

import android.graphics.Color

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/17
 * @description : 标准接口
 * </pre>
 */
interface ModelView {
    fun showToast(msg: String)

    fun showLoading(color: Int = Color.BLUE, tip: String = " 正在加载中 ... ", title: String = "请等待")

    fun hideLoading()

    fun onError(throwable: Throwable)
}