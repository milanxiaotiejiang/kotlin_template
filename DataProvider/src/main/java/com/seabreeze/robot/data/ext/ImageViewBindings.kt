package com.seabreeze.robot.data.ext

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.seabreeze.robot.data.GlideApp
import com.seabreeze.robot.data.R
import kotlin.random.Random

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/14
 * </pre>
 */
@BindingAdapter("imageUrl", "placeholder", requireAll = false)
fun ImageView.imageUrl(url: String?, @DrawableRes placeholder: Int?) {
    GlideApp.with(context)
        .load(url)
        .placeholder(
            placeholder?.let {
                ContextCompat.getDrawable(context, it)
            } ?: run {
                val placeholdersColors = resources.getStringArray(R.array.placeholders)
                val placeholderColor =
                    placeholdersColors[Random.nextInt(placeholdersColors.size)]
                ColorDrawable(Color.parseColor(placeholderColor))
            }
        )
        .into(this)
}

@BindingAdapter(
    value = ["imageUrl", "placeholder", "error", "fallback", "loadWidth", "loadHeight", "cacheEnable"],
    requireAll = false
)
fun ImageView.setImageUrl(
    source: Any? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    fallback: Drawable? = null,
    width: Int? = -1,
    height: Int? = -1,
    cacheEnable: Boolean? = true
) {
    // 计算位图尺寸，如果位图尺寸固定，加载固定大小尺寸的图片，如果位图未设置尺寸，那就加载原图，Glide加载原图时，override参数设置 -1 即可。
    val widthSize = (if ((width ?: 0) > 0) width else width) ?: -1
    val heightSize = (if ((height ?: 0) > 0) height else height) ?: -1
    // 根据定义的 cacheEnable 参数来决定是否缓存
    val diskCacheStrategy =
        if (cacheEnable == true) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE
    // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
    val encodeFormat =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG
    GlideApp.with(context)
        .asDrawable()
        .load(source)
        .placeholder(placeholder)
        .error(error)
        .fallback(fallback)
        .thumbnail(0.33f)
        .override(widthSize, heightSize)
        .skipMemoryCache(false)
        .sizeMultiplier(0.5f)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .encodeFormat(encodeFormat)
        .encodeQuality(80)
        .diskCacheStrategy(diskCacheStrategy)
        .transform()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}