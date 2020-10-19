package com.seabreeze.robot.base.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object Utils {
    private var sCurProcessName: String? = null

    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val processName = getCurProcessName(context)
        return if (processName != null && processName.contains(":")) {
            false
        } else processName != null && processName == context.packageName
    }

    private fun getCurProcessName(context: Context): String? {
        val processName = sCurProcessName
        if (!TextUtils.isEmpty(processName)) {
            return processName
        }
        try {
            val pid = Process.myPid()
            val mActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName
                    return sCurProcessName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sCurProcessName = curProcessNameFromProcess
        return sCurProcessName
    }

    // ignore
    // ignore
    private val curProcessNameFromProcess: String?
        get() {
            BufferedReader(
                InputStreamReader(
                    FileInputStream(
                        "/proc/" + Process.myPid() + "/cmdline"
                    ),
                    "iso-8859-1"
                )
            ).use { cmdlineReader ->
                var c: Int
                val processName = StringBuilder()
                while (cmdlineReader.read().also { c = it } > 0) {
                    processName.append(c.toChar())
                }
                return processName.toString()
            }
        }
}