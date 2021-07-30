@file:JvmName("FileUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.Context
import android.os.Environment
import com.seabreeze.robot.base.common.AppContext
import java.io.File
import java.io.FileInputStream

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/10/19
 * @description : TODO
 * </pre>
 */
/**
 * 检查sd卡是否可用
 */
fun isSdCardAvailable(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

/**
 * 获取SD根文件
 */
fun getSDRootFile(): File? {
    return if (isSdCardAvailable()) {
        AppContext.getExternalFilesDir(null)
    } else {
        null
    }
}

/**
 * @return 公共下载文件夹
 */
fun getPublicDownloadDir(): String {
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
}

/**
 * 获取指定文件大小(单位：字节)
 */
fun File.getFileSize(): Long {
    var size: Long = 0
    if (exists()) {
        FileInputStream(this).use {
            size = it.available().toLong()
        }
    }
    return size
}

fun Context.diskCachePath(): String? {
    return if (isSdCardAvailable() || !Environment.isExternalStorageRemovable()) {
        externalCacheDir?.path
    } else {
        cacheDir.path
    }
}