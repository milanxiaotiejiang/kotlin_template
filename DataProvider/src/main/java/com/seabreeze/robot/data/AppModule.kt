package com.seabreeze.robot.data

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream

/**
 * User: milan
 * Time: 2020/4/24 17:09
 * Des:
 */
@GlideModule
class AppModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) { //获取最大可用内存
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        //设置缓存的大小
        val cacheSize = maxMemory / 8
        // Default empty impl.
        //设置Bitmap的缓存池
        builder.setBitmapPool(LruBitmapPool(30))
        //设置内存缓存
        builder.setMemoryCache(LruResourceCache(cacheSize.toLong()))
        //设置磁盘缓存
        builder.setDiskCache(InternalCacheDiskCacheFactory(context))
        //设置读取不在缓存中资源的线程
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor())
        //设置读取磁盘缓存中资源的线程
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor())
        //设置日志级别
        builder.setLogLevel(Log.VERBOSE)
        //设置全局选项
        val requestOptions = RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)
        builder.setDefaultRequestOptions(requestOptions)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(DataApplication.okHttpClient)
        )
    }
}