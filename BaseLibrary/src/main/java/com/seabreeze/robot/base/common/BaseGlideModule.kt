package com.seabreeze.robot.base.common

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
@GlideModule
class BaseGlideModule : LibraryGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {}
}