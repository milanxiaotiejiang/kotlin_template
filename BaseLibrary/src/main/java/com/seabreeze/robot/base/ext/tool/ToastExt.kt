@file:JvmName("ToastUtil")

package com.seabreeze.robot.base.ext.tool

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.seabreeze.robot.base.common.AppContext
import me.drakeet.support.toast.ToastCompat
import java.util.*

/**
 * User: milan
 * Time: 2019/9/10 15:11
 * Des:
 */
fun Context.toast(value: String) = toast { value }

inline fun toast(value: () -> String) {
    val toast = ToastCompat.makeText(AppContext, value(), Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.setText(value())
    toast.show()
}

fun Context.toast(value: String, delay: Long = 500) = toast({ value }, delay)

inline fun toast(value: () -> String, delay: Long) {
    val toast = Toast.makeText(AppContext, value(), Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.setText(value())

    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            toast.show()
        }
    }, 0, delay + 500)
    Timer().schedule(object : TimerTask() {
        override fun run() {
            toast.cancel()
            timer.cancel()
        }
    }, delay)
}

