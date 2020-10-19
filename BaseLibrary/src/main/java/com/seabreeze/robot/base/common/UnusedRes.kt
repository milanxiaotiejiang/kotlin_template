package com.seabreeze.robot.base.common

import android.content.Context
import android.view.LayoutInflater
import com.seabreeze.robot.base.R

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : 仅仅保存 xml 不被删除
 * </pre>
 */
fun Context.unUsed() {
    LayoutInflater.from(this).inflate(R.layout.toolbar, null)
    LayoutInflater.from(this).inflate(R.layout.status_bar, null)
}
