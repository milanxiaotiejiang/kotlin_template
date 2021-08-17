package com.seabreeze.robot.data.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import com.seabreeze.robot.base.ext.foundation.DispatcherExecutor
import com.seabreeze.robot.data.GlideApp
import com.seabreeze.robot.data.R
import java.io.File
import java.math.BigDecimal


/**
 * <pre>
 * author : 76515
 * time   : 2020/7/2
 * desc   :
</pre> *
 */
class GlideEngine private constructor() : ImageEngine, com.luck.picture.lib.engine.ImageEngine {

    /**
     *  com.luck.picture.lib.engine
     * */

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        GlideApp.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.picture_image_placeholder)
                    .error(R.drawable.picture_image_placeholder)
            )
            .into(imageView)
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: com.luck.picture.lib.listener.OnImageCompleteCallback?
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .into(object : ImageViewTarget<Bitmap?>(imageView) {
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    callback?.onShowLoading()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback?.onHideLoading()
                }

                override fun setResource(resource: Bitmap?) {
                    callback?.onHideLoading()
                    if (resource != null) {
                        val eqLongImage = isLongImg(
                            resource.width,
                            resource.height
                        )
                        longImageView?.apply {
                            visibility = if (eqLongImage) View.VISIBLE else View.GONE
                            imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                            if (eqLongImage) {
                                // 加载长图
                                isQuickScaleEnabled = true
                                isZoomEnabled = true
                                isPanEnabled = true
                                setDoubleTapZoomDuration(100)
                                setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                                setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                                setImage(
                                    ImageSource.bitmap(resource),
                                    ImageViewState(0F, PointF(0F, 0F), 0)
                                )
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource)
                            }
                        }
                    }
                }
            })
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .into(object : ImageViewTarget<Bitmap?>(imageView) {
                override fun setResource(resource: Bitmap?) {
                    if (resource != null) {
                        val eqLongImage = isLongImg(
                            resource.width,
                            resource.height
                        )
                        longImageView?.apply {
                            visibility = if (eqLongImage) View.VISIBLE else View.GONE
                            imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                            if (eqLongImage) {
                                // 加载长图
                                isQuickScaleEnabled = true
                                isZoomEnabled = true
                                isPanEnabled = true
                                setDoubleTapZoomDuration(100)
                                setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                                setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                                setImage(
                                    ImageSource.bitmap(resource),
                                    ImageViewState(0F, PointF(0F, 0F), 0)
                                )
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource)
                            }
                        }
                    }
                }
            })
    }

    /**
     *  com.luck.picture.lib.engine
     * */

    /**
     * 加载图片 一般用此方法即可
     * 加载图片 自定义
     *
     * @param context
     * @param url
     * @param imageView
     */
    override fun loadAnyImage(
        context: Context,
        url: Any?,
        @DrawableRes resourceId: Int,
        imageView: ImageView
    ) {
        GlideApp.with(context)
            .load(url)
            .thumbnail()
            .apply(
                RequestOptions()
                    .placeholder(resourceId)
                    .error(resourceId)
            )
            .into(imageView)
    }

    override fun loadImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        GlideApp.with(context)
            .load(uri)
            .override(resizeX, resizeY)
            .priority(Priority.HIGH)
            .fitCenter()
            .into(imageView)
    }


    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadFolderImage(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .centerCrop()
            .sizeMultiplier(0.5f)
            .apply(RequestOptions().placeholder(R.drawable.picture_image_placeholder))
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    /**
     * 加载gif
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAsGifImage(
        context: Context, url: String,
        imageView: ImageView
    ) {
        GlideApp.with(context)
            .asGif()
            .load(url)
            .into(imageView)
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        GlideApp.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .apply(RequestOptions().placeholder(R.drawable.picture_image_placeholder))
            .into(imageView)
    }

    /**
     * 缩略图方向
     */
    override fun loadThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(placeholder)
            .override(resize, resize)
            .centerCrop()
            .into(imageView)
    }

    override fun loadGifThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(placeholder)
            .override(resize, resize)
            .centerCrop()
            .into(imageView)
    }

    override fun loadGifImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        GlideApp.with(context)
            .asGif()
            .load(uri)
            .override(resizeX, resizeY)
            .priority(Priority.HIGH)
            .into(imageView)
    }

    override fun loadImageCallback(
        context: Context,
        url: String,
        imageView: ImageView,
        callback: OnImageCompleteCallback?
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
//            .error(R.drawable.picture_image_placeholder)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.picture_image_placeholder)
                    .error(R.drawable.picture_image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
            )
            .into(object : ImageViewTarget<Bitmap?>(imageView) {
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    callback?.onShowLoading()
                    if (placeholder != null) {
                        imageView.setImageDrawable(placeholder)
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback?.onHideLoading()
                    if (errorDrawable != null) {
                        imageView.setImageDrawable(errorDrawable)
                    }
                }

                override fun setResource(resource: Bitmap?) {
                    callback?.onHideLoading()
                    if (resource != null) {
                        imageView.setImageBitmap(resource)
                    }
                }
            })
    }

    override fun supportAnimatedGif() = true

    /**
     * 清除图片磁盘缓存
     */
    override fun clearImageDiskCache(activity: AppCompatActivity) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            DispatcherExecutor.iOExecutor.submit {
                GlideApp.get(activity).clearDiskCache()
            }
        } else {
            GlideApp.get(activity).clearDiskCache()
        }
    }

    /**
     * 清除图片内存缓存
     */
    override fun clearImageMemoryCache(activity: AppCompatActivity) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            DispatcherExecutor.iOExecutor.submit {
                GlideApp.get(activity).clearMemory()
            }
        } else {
            GlideApp.get(activity).clearMemory()
        }
    }

    override fun clearImageAllCache(activity: AppCompatActivity) {//暂时这样，不完美
        clearImageDiskCache(activity)
        clearImageMemoryCache(activity)
        deleteFolderFile(
            activity.externalCacheDir.toString() + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR,
            true
        )
    }

    override fun getCacheSize(context: Context): String {
        val file = File(
            context.cacheDir.absolutePath,
            InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR
        )
        return getFormatSize(getFolderSize(file).toDouble())
    }

    /**
     * /data/user/0/com.thirtydays.power/cache/image_manager_disk_cache
     */
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        val listFiles = file.listFiles()
        listFiles?.forEach {
            size += if (it.isDirectory) {
                getFolderSize(it)
            } else {
                it.length()
            }
        }
        return size
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1)
            return size.toString() + "Byte"

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString().toString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString().toString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString().toString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString().toString() + "TB"
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (filePath.isNotEmpty()) {
            val file = File(filePath)
            if (file.isDirectory) {
                file.listFiles().forEach {
                    deleteFolderFile(it.absolutePath, true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {
                    file.delete()
                } else {
                    if (file.listFiles().isEmpty())
                        file.delete()
                }
            }

        }
    }

    /**
     * 是否是长图
     *
     * @param width  宽
     * @param height 高
     * @return true 是 or false 不是
     */
    private fun isLongImg(width: Int, height: Int): Boolean {
        val newHeight = width * 3
        return height > newHeight
    }

    companion object {
        val INSTANCE: GlideEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideEngine()
        }
    }
}