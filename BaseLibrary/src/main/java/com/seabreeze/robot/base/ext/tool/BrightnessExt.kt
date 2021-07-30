@file:JvmName("BrightnessUtil")

package com.seabreeze.robot.base.ext.tool

import android.Manifest
import android.app.Activity
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.seabreeze.robot.base.common.AppContext

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 屏幕亮度工具类
 * </pre>
 */
/**
 * 是否开启了自动亮度
 */
val isAutoBrightness: Boolean
    get() = try {
        Settings.System.getInt(
            AppContext.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE
        ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        false
    }


/**
 * 设置是否开启自动亮度
 * @param enable : 为true时开启，false时关闭
 * @return 设置成功返回true
 */
fun setAutoBrightness(enable: Boolean) = Settings.System.putInt(
    AppContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
    if (enable) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
)


/**
 * 系统屏幕亮度，需要WRITE_SETTINGS权限，并在代码中申请系统设置权限
 * 范围为0~255
 */
var systemBrightness
    get() = try {
        Settings.System.getInt(
            AppContext.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        -1
    }
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    set(brightness) {
        if (isAutoBrightness) {
            //如果当前是自动亮度，则关闭自动亮度
            setAutoBrightness(false)
        }
        val uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)
        Settings.System.putInt(
            AppContext.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            brightness
        )
        AppContext.contentResolver.notifyChange(uri, null)
    }

/**
 * 当前窗口亮度
 * 范围为0~1.0,1.0时为最亮，-1为系统默认设置
 */
var Activity.windowBrightness
    get() = window.attributes.screenBrightness
    set(brightness) {
        //小于0或大于1.0默认为系统亮度
        window.attributes = window.attributes.apply {
            screenBrightness = if (brightness > 1.0 || brightness < 0) -1.0F else brightness
        }
    }