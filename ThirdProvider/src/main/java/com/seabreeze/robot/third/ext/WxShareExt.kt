package com.seabreeze.robot.third.ext

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.tencent.mm.opensdk.modelmsg.*
import com.seabreeze.robot.third.ext.WxPay.createWx
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * User: milan
 * Time: 2020/9/14 20:34
 * Des:
 */
/*
SendMessageToWX.Req.WXSceneSession
SendMessageToWX.Req.WXSceneTimeline
SendMessageToWX.Req.WXSceneFavorite
 */

private const val THUMB_SIZE: Int = 150

fun RxAppCompatActivity.shareWxText(
    text: String,
    title: String = "",
    description: String = "",
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val textObj = WXTextObject()
    textObj.text = text

    val msg = WXMediaMessage()
    msg.mediaObject = textObj

    msg.title = title
    msg.description = description

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("text")
    req.message = msg
    req.scene = scene

    createWx()?.sendReq(req)
}

fun RxAppCompatActivity.shareWxImg(
    bmp: Bitmap,
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val imgObj = WXImageObject(bmp)

    val msg = WXMediaMessage()
    msg.mediaObject = imgObj

    msg.thumbData = thumbData(bmp)

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("img")
    req.message = msg
    req.scene = scene
    createWx()?.sendReq(req)
}

fun RxAppCompatActivity.shareWxImg(
    path: String,
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val file = File(path)
    if (!file.exists()) {
        return
    }

    val imgObj = WXImageObject()
    imgObj.setImagePath(path)

    val msg = WXMediaMessage()
    msg.mediaObject = imgObj

    val bmp = BitmapFactory.decodeFile(path)
    msg.thumbData = thumbData(bmp)

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("img")
    req.message = msg
    req.scene = scene
    createWx()?.sendReq(req)
}

fun RxAppCompatActivity.shareWxMusic(
    musicUrl: String,
    title: String = "",
    description: String = "",
    bmp: Bitmap,
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val music = WXMusicObject()
    music.musicUrl = musicUrl

    val msg = WXMediaMessage()
    msg.mediaObject = music
    msg.title = title
    msg.description = description

    msg.thumbData = thumbData(bmp)

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("music")
    req.message = msg
    req.scene = scene
    createWx()?.sendReq(req)
}

fun RxAppCompatActivity.shareWxVideo(
    videoUrl: String,
    title: String = "",
    description: String = "",
    messageAction: String = "",
    bmp: Bitmap,
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val video = WXVideoObject()
    video.videoUrl = videoUrl

    val msg = WXMediaMessage(video)

    msg.messageAction = messageAction

    msg.title = title
    msg.description = description

    msg.thumbData = thumbData(bmp)

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("video")
    req.message = msg
    req.scene = scene
    createWx()?.sendReq(req)
}

fun RxAppCompatActivity.shareWxWebpage(
    webpageUrl: String,
    title: String = "",
    description: String = "",
    bmp: Bitmap,
    scene: Int = SendMessageToWX.Req.WXSceneSession
) {
    val webpage = WXWebpageObject()
    webpage.webpageUrl = webpageUrl
    val msg = WXMediaMessage(webpage)

    msg.title = title
    msg.description = description

    msg.thumbData = thumbData(bmp)

    val req = SendMessageToWX.Req()
    req.transaction = buildTransaction("webpage")
    req.message = msg
    req.scene = scene
    createWx()?.sendReq(req)
}

private fun thumbData(
    bmp: Bitmap
): ByteArray {
    val dstHeight = THUMB_SIZE * bmp.height / bmp.width
    val thumbBmp = Bitmap.createScaledBitmap(
        bmp, THUMB_SIZE, dstHeight, true
    )
    bmp.recycle()
    return bmpToByteArray(thumbBmp, true)
}

private fun buildTransaction(type: String): String {
    return type + System.currentTimeMillis()
}

fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
    val output = ByteArrayOutputStream()
    bmp.compress(CompressFormat.PNG, 100, output)
    if (needRecycle) {
        bmp.recycle()
    }
    val result = output.toByteArray()
    output.close()
    return result
}