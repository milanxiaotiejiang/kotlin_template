package com.thirtydays.kotlin.widget

import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.seabreeze.robot.base.widget.pop.CustomPopWindow
import com.seabreeze.robot.base.widget.pop.CustomPopWindow.PopupWindowBuilder
import com.thirtydays.kotlin.R

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/23
 * desc   :
</pre> *
 */
fun Fragment.showHelpPopWindow(feedback: () -> Unit, consumer: () -> Unit, website: () -> Unit) {
    val popWindow = PopupWindowBuilder(context)
        .setView(R.layout.pop_help_layout)
        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        .setOutsideTouchable(true)
        .create()

    popWindow.popupWindow.contentView.findViewById<RelativeLayout>(R.id.feedbackLayout)
        .setOnClickListener {
            feedback()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<RelativeLayout>(R.id.consumerLayout)
        .setOnClickListener {
            consumer()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<RelativeLayout>(R.id.websiteLayout)
        .setOnClickListener {
            website()
            popWindow.dissmiss()
        }

    popWindow.showAtLocation(Gravity.BOTTOM, 0, 0)
}

fun Activity.showChoosePhotoPopWindow(album: () -> Unit, take: () -> Unit) {
    val popWindow: CustomPopWindow = PopupWindowBuilder(this)
        .setView(R.layout.pop_choose_photo_layout)
        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        .setOutsideTouchable(true)
        .enableBackgroundDark(true)
        .setBgDarkAlpha(0.5f)
        .create()
    popWindow.popupWindow.contentView.findViewById<TextView>(R.id.tvAlbum)
        .setOnClickListener {
            album()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<TextView>(R.id.tvTake)
        .setOnClickListener {
            take()
            popWindow.dissmiss()
        }
    popWindow.popupWindow.contentView.findViewById<TextView>(R.id.tvCancel)
        .setOnClickListener {
            popWindow.dissmiss()
        }
    popWindow.showAtLocation(Gravity.BOTTOM, 0, 0)
}
