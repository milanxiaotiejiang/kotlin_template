package com.seabreeze.robot.third.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.elvishew.xlog.XLog
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ext.*
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.ext.createQRCodeBitmap
import com.seabreeze.robot.third.ext.shareQQImage
import com.seabreeze.robot.third.ext.shareWxImg
import com.seabreeze.robot.third.pop.showSharePopWindow
import com.tencent.connect.common.Constants
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_share_pic_model.*
import java.io.BufferedOutputStream
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/18
 * @description : TODO
 * </pre>
 */
@Route(path = RouterPath.UserCenter.PATH_APP_SHARE)
class ShareActivity : RxAppCompatActivity() {

    private val iuiListener = object : IUiListener {
        override fun onComplete(response: Any?) {
            XLog.e("QQ分享成功")
        }

        override fun onError(error: UiError?) {
            XLog.e(error)
        }

        override fun onCancel() {
            XLog.e("取消QQ分享")
        }

    }

    private var mPosterPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        addDisposable(save.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                Flowable.just(posterLayout)
                    .map { view ->
                        val width: Int = view.width
                        val height: Int = view.height
                        screenWidth
                        screenHeight

                        val bitmapWidth = min(width, screenWidth)
                        val bitmapHeight = min(height, screenHeight)

                        view.layout(0, 0, bitmapWidth, bitmapHeight)


                        val bitmap = Bitmap.createBitmap(
                            bitmapWidth,
                            bitmapHeight,
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = Canvas(bitmap)
                        view.draw(canvas)
                        return@map bitmap
                    }
                    .map { bitmap ->

                        val posterFileDir = File(externalCacheDir, "poster")
                        if (!posterFileDir.exists() || !posterFileDir.isDirectory) {
                            posterFileDir.mkdirs()
                        }
                        val posterFile = File(posterFileDir.absolutePath, "$currentTimeMillis.png")
                        if (!posterFile.exists()) {
                            posterFile.createNewFile()
                        }

                        posterFile.outputStream().use {
                            val bos = BufferedOutputStream(it)
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                            bos.flush()
                            bos.close()
                        }
                        return@map posterFile.absolutePath
                    }
                    .execute(this)
                    .subscribe {
                        toast { "海报生成成功" }
                        mPosterPath = it
                    }
            }
        )

        addDisposable(share.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                mPosterPath?.let {
                    showSharePopWindow({
                        shareWxImg(it)
                    }, {
                        shareWxImg(it, scene = SendMessageToWX.Req.WXSceneTimeline)
                    }, {
                        shareQQImage(it, iuiListener)
                    })
                } ?: toast { "请先保存海报" }
            }
        )

        qRCode.setImageBitmap("765151629@qq.com".createQRCodeBitmap(dp2px(100), dp2px(100)))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iuiListener)
        }
    }
}