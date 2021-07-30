@file:JvmName("EventUtil")

package com.seabreeze.robot.base.ext.tool

import android.app.Activity
import androidx.fragment.app.Fragment
import com.seabreeze.robot.base.model.BaseEvent
import org.greenrobot.eventbus.EventBus

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/3
 * desc   :
</pre> *
 */
fun Activity.registerEvent() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Activity.unregisterEvent() {
    EventBus.getDefault().unregister(this)
}

fun Fragment.registerEvent() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Fragment.unregisterEvent() {
    EventBus.getDefault().unregister(this)
}

fun <T> postEvent(event: BaseEvent<T>) {
    EventBus.getDefault().post(event)
}