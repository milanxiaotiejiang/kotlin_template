package com.thirtydays.kotlin.ui.glide

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.elvishew.xlog.XLog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.permissionx.guolindev.PermissionX
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.seabreeze.robot.data.utils.image.GlideEngine
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.ChoseImage
import com.thirtydays.kotlin.adapter.ChoseImageAdapter
import com.thirtydays.kotlin.widget.showChoosePhotoPopWindow
import kotlinx.android.synthetic.main.activity_glide.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * User: milan
 * Time: 2020/9/18 22:47
 * Des:
 */
class GlideActivity : RxAppCompatActivity() {

    private val mAdapter = ChoseImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        tvLoad.setOnClickListener {
            showPopWindow()
        }
        tvClear.setOnClickListener {
            GlideEngine.INSTANCE.clearImageAllCache(this)
        }
        tvSize.setOnClickListener {
            toast { GlideEngine.INSTANCE.getCacheSize(this) }
        }

        rvImage.layoutManager = GridLayoutManager(this, 3)
        rvImage.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.ivDelete, R.id.image)
        mAdapter.setOnItemChildClickListener { _, view, position ->

            if (view.id == R.id.image) {
                val choseImage = mAdapter.data[position]
                if (choseImage.itemType != 0) {
                    showPopWindow()
                }
            } else if (view.id == R.id.ivDelete) {
                mAdapter.removeAt(position)
                val lastChoseImage = mAdapter.data[mAdapter.data.size - 1]
                if (lastChoseImage.itemType == 0) {
                    mAdapter.addData(ChoseImage("", -1))
                }
            }
        }

        mAdapter.addData(ChoseImage("", -1))
    }


    private val maxNum = 6

    private fun maxSelectNum(): Int {
        return maxNum + 1 - mAdapter.data.size
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val obtainMultipleResult = PictureSelector.obtainMultipleResult(data)
                        .apply {
                            if (size > maxNum)
                                take(maxNum)
                        }

                    for (i in obtainMultipleResult.indices) {
                        val localMedia = obtainMultipleResult[i]
                        Luban.with(this)
                            .load(localMedia.cutPath)
                            .ignoreBy(100)
                            .setCompressListener(object : OnCompressListener {
                                override fun onSuccess(file: File?) {
                                    if (file != null) {
                                        localMedia.compressPath = file.absolutePath
                                        successively(localMedia)
                                    } else {
                                        toast { "压缩失败" }
                                    }
                                }

                                override fun onError(e: Throwable?) {
                                    XLog.e(e?.message)
                                }

                                override fun onStart() {
                                }
                            }).launch() //启动压缩
                    }
                    XLog.e(obtainMultipleResult)
                }
                PictureConfig.REQUEST_CAMERA -> {
                    val localMedia = PictureSelector.obtainMultipleResult(data).first()
                    successively(localMedia)
                }
            }
        }
    }

    private fun successively(localMedia: LocalMedia) {
        mAdapter.addData(mAdapter.data.size - 1, ChoseImage(localMedia.compressPath))
        if (mAdapter.data.size == maxNum + 1) {
            mAdapter.removeAt(mAdapter.data.size - 1)
        }
    }

    private fun showPopWindow() {
        showChoosePhotoPopWindow({

            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxSelectNum())
                .imageEngine(GlideEngine.INSTANCE)
                .isEnableCrop(true)
                .isCompress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }, {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        PictureSelector.create(this)
                            .openCamera(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.INSTANCE)
                            .isEnableCrop(true)
                            .isCompress(true)
                            .forResult(PictureConfig.REQUEST_CAMERA);
                    } else {
                        toast { "请在设置中开启相机权限" }
                    }
                }
        })
    }
}