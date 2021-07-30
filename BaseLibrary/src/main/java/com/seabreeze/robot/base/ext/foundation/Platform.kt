package com.seabreeze.robot.base.ext.foundation

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * User: milan
 * Time: 2019/7/2 10:02
 * Des:
 */
open class Platform {
    open fun defaultCallbackExecutor(): Executor {
        return Executors.newCachedThreadPool()
    }

    fun execute(runnable: Runnable) {
        defaultCallbackExecutor().execute(runnable)
    }

    internal class Android : Platform() {
        override fun defaultCallbackExecutor(): Executor {
            return MainThreadExecutor()
        }

        internal class MainThreadExecutor : Executor {
            private val handler = Handler(Looper.getMainLooper())
            override fun execute(r: Runnable) {
                handler.post(r)
            }
        }
    }

    companion object {
        private val PLATFORM = findPlatform()
        fun get(): Platform {
            return PLATFORM
        }

        private fun findPlatform(): Platform {
            try {
                Class.forName("android.os.Build")
                if (Build.VERSION.SDK_INT != 0) {
                    return Android()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            return Platform()
        }
    }
}