package com.seabreeze.robot.data.utils.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.seabreeze.robot.data.R

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/17
 * desc   :
</pre> *
 */
interface ImageEngine {

    /**
     * Loading image
     */
    fun loadAnyImage(
        context: Context, url: Any?,
        @DrawableRes resourceId: Int = R.drawable.picture_image_placeholder, imageView: ImageView
    )

    /**
     * Load a static image resource.
     *
     * @param context   Context
     * @param resizeX   Desired x-size of the origin image
     * @param resizeY   Desired y-size of the origin image
     * @param imageView ImageView widget
     * @param uri       Uri of the loaded image
     */
    fun loadImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri)

    /**
     * Load album catalog pictures
     */
    fun loadFolderImage(context: Context, url: String, imageView: ImageView)

    /**
     * Load GIF image
     */
    fun loadAsGifImage(context: Context, url: String, imageView: ImageView)

    /**
     * Load picture list picture
     */
    fun loadGridImage(context: Context, url: String, imageView: ImageView)

    //*************************other*****************************

    /**
     * Load thumbnail of a static image resource.
     *
     * @param context     Context
     * @param resize      Desired size of the origin image
     * @param placeholder Placeholder drawable when image is not loaded yet
     * @param imageView   ImageView widget
     * @param uri         Uri of the loaded image
     */
    fun loadThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    )

    /**
     * Load thumbnail of a gif image resource. You don't have to load an animated gif when it's only
     * a thumbnail tile.
     *
     * @param context     Context
     * @param resize      Desired size of the origin image
     * @param placeholder Placeholder drawable when image is not loaded yet
     * @param imageView   ImageView widget
     * @param uri         Uri of the loaded image
     */
    fun loadGifThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    )

    /**
     * Load a gif image resource.
     *
     * @param context   Context
     * @param resizeX   Desired x-size of the origin image
     * @param resizeY   Desired y-size of the origin image
     * @param imageView ImageView widget
     * @param uri       Uri of the loaded image
     */
    fun loadGifImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri)

    fun loadImageCallback(
        context: Context,
        url: String,
        imageView: ImageView,
        callback: OnImageCompleteCallback? = null
    )

    /**
     * Whether this implementation supports animated gif.
     * Just knowledge of it, convenient for users.
     *
     * @return true support animated gif, false do not support animated gif.
     */
    fun supportAnimatedGif(): Boolean

    fun clearImageDiskCache(activity: AppCompatActivity)

    fun clearImageMemoryCache(activity: AppCompatActivity)

    fun clearImageAllCache(activity: AppCompatActivity)

    fun getCacheSize(context: Context): String

}