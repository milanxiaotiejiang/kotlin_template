package com.thirtydays.kotlin.ui.ext.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.*
import com.thirtydays.kotlin.R
import java.util.*

class TimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)


        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.MONTH, -1)
        val m = c.time

        val getTimeFormatStr = getTimeFormatStr(FORMAT_YMDHMS, m)
        XLog.e("getTimeFormatStr $getTimeFormatStr")

        XLog.e("currentYear $currentYear")
        XLog.e("currentYear $currentMonth")
        XLog.e("currentYear $currentDay")
        XLog.e("currentYear $currentTimeMillis")

        XLog.e("formatCurrentDate ${formatCurrentDate()}")
        XLog.e("formatCurrentDateTime ${formatCurrentDateTime()}")
        XLog.e("formatCurrentTime ${formatCurrentTime()}")
        XLog.e("formatTimeY ${getTimeFormatStr.formatTimeY()}")
        XLog.e("formatTimeMM ${getTimeFormatStr.formatTimeMM()}")
        XLog.e("formatTimeM ${getTimeFormatStr.formatTimeM()}")
        XLog.e("formatTimeYM ${getTimeFormatStr.formatTimeYM()}")
        XLog.e("formatTimeDD ${getTimeFormatStr.formatTimeDD()}")
        XLog.e("formatTimeD ${getTimeFormatStr.formatTimeD()}")
        XLog.e("formatTimeYMD ${getTimeFormatStr.formatTimeYMD()}")
        XLog.e("formatTimeYMDChinese ${getTimeFormatStr.formatTimeYMDChinese()}")
        XLog.e("formatTimeHM ${getTimeFormatStr.formatTimeHM()}")
        XLog.e("formatTimeHMS ${getTimeFormatStr.formatTimeHMS()}")
        XLog.e("formatTimeYMDHM ${getTimeFormatStr.formatTimeYMDHM()}")
        XLog.e("formatTimeYMDHMS ${getTimeFormatStr.formatTimeYMDHMS()}")
        XLog.e("formatTimeWeek ${getTimeFormatStr.formatTimeWeek()}")
    }
}