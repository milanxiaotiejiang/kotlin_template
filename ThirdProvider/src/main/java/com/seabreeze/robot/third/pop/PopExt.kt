package com.seabreeze.robot.third.pop

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.seabreeze.robot.base.widget.pop.CustomPopWindow
import com.seabreeze.robot.third.R

/**
 * User: milan
 * Time: 2020/9/18 23:34
 * Des:
 */

fun AppCompatActivity.showSharePopWindow(
    wechat: () -> Unit,
    moments: () -> Unit,
    qq: () -> Unit
) {
    val popWindow = CustomPopWindow.PopupWindowBuilder(this)
        .setView(R.layout.pop_share_layout)
        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        .setOutsideTouchable(true)
        .enableBackgroundDark(true)
        .setBgDarkAlpha(0.5f)
        .create()

    popWindow.popupWindow.contentView.findViewById<LinearLayout>(R.id.wechatLayout)
        .setOnClickListener {
            wechat()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<LinearLayout>(R.id.momentsLayout)
        .setOnClickListener {
            moments()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<LinearLayout>(R.id.microblogLayout)
        .setOnClickListener {
            qq()
            popWindow.dissmiss()
        }

    popWindow.showAtLocation(Gravity.BOTTOM, 0, 0)
}