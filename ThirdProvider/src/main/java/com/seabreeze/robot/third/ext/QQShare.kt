package com.seabreeze.robot.third.ext

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seabreeze.robot.base.ext.tool.getResString
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.ext.QQLogin.createQQ
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 * User: milan
 * Time: 2020/9/14 22:32
 * Des: 当前应用进程与QQ都杀掉的情况下，当前应用程序分享到QQ我的电脑后，返回再次进入QQ会持续分享
 */
/*
QQShare.SHARE_TO_QQ_EXT_INT
    QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN 、QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE

SHARE_TO_QQ_TYPE_DEFAULT = 1;
SHARE_TO_QQ_TYPE_AUDIO = 2;
SHARE_TO_QQ_TYPE_IMAGE = 5;
SHARE_TO_QQ_TYPE_APP = 6;
SHARE_TO_QQ_MINI_PROGRAM = 7;
 */

fun AppCompatActivity.shareQQMusic(
    activity: Activity,
    title: String,
    targetUrl: String = "",
    summary: String = "",
    imageUrl: String = "",
    arkStr: String = "",
    audioUrl: String
) {
    val params = Bundle()
        .apply {
            standard(title, targetUrl, summary, imageUrl)
            toHaveMust(arkStr = arkStr)
            putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl)
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)
        }


    createQQ().shareToQQ(activity, params, object : IUiListener {
        override fun onComplete(response: Any?) {
        }

        override fun onError(error: UiError?) {
        }

        override fun onCancel() {
        }

    })
}

fun AppCompatActivity.shareQQImage(imageUrl: String, listener: IUiListener) {
    val params = Bundle()
        .apply {
            putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl)
            toHaveMust(isArk = false)

            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
        }

    createQQ().shareToQQ(this, params, listener)
}

fun AppCompatActivity.shareQQDefault(
    activity: Activity,
    title: String,
    targetUrl: String = "",
    summary: String = "",
    imageUrl: String = "",
    arkStr: String = ""
) {
    val params = Bundle()
        .apply {
            standard(title, targetUrl, summary, imageUrl)
            toHaveMust(arkStr = arkStr)
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        }
    createQQ().shareToQQ(activity, params, object : IUiListener {
        override fun onComplete(response: Any?) {
        }

        override fun onError(error: UiError?) {
        }

        override fun onCancel() {
        }

    })
}

private fun Bundle.toHaveMust(isArk: Boolean = true, arkStr: String = "") {
    putString(QQShare.SHARE_TO_QQ_APP_NAME, getResString(R.string.app_name))//qq返回字段
    putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0)//QQShare.SHARE_TO_QQ_EXT_INT
    if (isArk) {
        putString(QQShare.SHARE_TO_QQ_ARK_INFO, arkStr)
    }
}

private fun Bundle.standard(
    title: String,
    targetUrl: String,
    summary: String,
    imageUrl: String
) {
    putString(QQShare.SHARE_TO_QQ_TITLE, title)
    putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl)
    putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
    putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
}