package com.seabreeze.robot.base.common.handler

import android.os.Message
import java.util.concurrent.ConcurrentHashMap

internal object GetDetailHandlerHelper {
    val msgDetail = ConcurrentHashMap<Message, String>()
}