@file:JvmName("TimeConvertUtil")

package com.seabreeze.robot.base.ext.tool

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 时间日期工具类 (主要负责日期转相关文字，偏展示)
 * </pre>
 */
private const val ONE_MINUTE_MILLIONS = 60 * 1000.toLong()
private const val ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS

/**
 * 获取短时间格式
 *
 * @return
 */
fun getShortTime(millis: Long): String {
    val durTime = Date().time - Date(millis).time
    val dayStatus = calculateDayStatus(Date(millis), Date())
    return if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
        "刚刚"
    } else if (durTime < ONE_HOUR_MILLIONS) {
        "${durTime / ONE_MINUTE_MILLIONS}分钟前"
    } else if (dayStatus == 0) {
        "${durTime / ONE_HOUR_MILLIONS}小时前"
    } else if (dayStatus == -1) {
        "昨天" + DateFormat.format("HH:mm", Date(millis))
    } else if (isSameYear(Date(millis), Date()) && dayStatus < -1) {
        DateFormat.format("MM-dd", Date(millis)).toString()
    } else {
        DateFormat.format("yyyy-MM", Date(millis)).toString()
    }
}

fun getGroupTime(millis: Long): String {
    val dayStatus = calculateDayStatus(Date(millis), Date())
    return if (dayStatus == 0) {
        "今天"
    } else if (dayStatus == -1) {
        "昨天"
    } else if (isSameYear(Date(millis), Date()) && dayStatus < -1) {
        DateFormat.format("MM-dd", Date(millis)).toString()
    } else {
        DateFormat.format("yyyy-MM", Date(millis)).toString()
    }
}

fun getItemTime(millis: Long): String {
    val durTime = Date().time - Date(millis).time
    val dayStatus = calculateDayStatus(Date(millis), Date())
    return if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
        "刚刚"
    } else if (durTime < ONE_HOUR_MILLIONS) {
        "${durTime / ONE_MINUTE_MILLIONS}分钟前"
    } else if (dayStatus == 0) {
        "${durTime / ONE_HOUR_MILLIONS}小时前"
    } else if (dayStatus == -1) {
        "昨天 " + DateFormat.format("HH:mm", Date(millis))
    } else if (isSameYear(Date(millis), Date()) && dayStatus < -1) {
        DateFormat.format("MM-dd HH:mm", Date(millis)).toString()
    } else {
        DateFormat.format("yyyy-MM-dd HH:mm", Date(millis)).toString()
    }
}

/**
 * 判断是否是同一年
 *
 * @param targetTime
 * @param compareTime
 * @return
 */
private fun isSameYear(targetTime: Date, compareTime: Date): Boolean {
    val tarCalendar = Calendar.getInstance()
    tarCalendar.time = targetTime
    val tarYear = tarCalendar[Calendar.YEAR]
    val compareCalendar = Calendar.getInstance()
    compareCalendar.time = compareTime
    val comYear = compareCalendar[Calendar.YEAR]
    return tarYear == comYear
}

/**
 * 判断是否处于今天还是昨天，0表示今天，-1表示昨天，小于-1则是昨天以前
 *
 * @param targetTime
 * @param compareTime
 * @return
 */
private fun calculateDayStatus(targetTime: Date, compareTime: Date): Int {
    val tarCalendar = Calendar.getInstance()
    tarCalendar.time = targetTime
    val tarDayOfYear = tarCalendar[Calendar.DAY_OF_YEAR]
    val compareCalendar = Calendar.getInstance()
    compareCalendar.time = compareTime
    val comDayOfYear = compareCalendar[Calendar.DAY_OF_YEAR]
    return tarDayOfYear - comDayOfYear
}

fun isToday(day: Long): Boolean {
    val pre = Calendar.getInstance()
    val predate = Date(System.currentTimeMillis())
    pre.time = predate
    val cal = Calendar.getInstance()
    val date = Date(day)
    cal.time = date
    if (cal[Calendar.YEAR] == pre[Calendar.YEAR]) {
        val diffDay = (cal[Calendar.DAY_OF_YEAR] - pre[Calendar.DAY_OF_YEAR])
        return diffDay == 0
    }
    return false
}

val dateFormat: SimpleDateFormat?
    get() {
        if (null == DateLocal.get()) {
            DateLocal.set(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
        }
        return DateLocal.get()
    }
private val DateLocal = ThreadLocal<SimpleDateFormat?>()

//获得当天0点时间
val timesMorning: Long
    get() {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis
    }

//获得当天24点时间
val timesNight: Long
    get() {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 24
        cal[Calendar.SECOND] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis / 1000
    }

val duringDay: String
    get() {
        val date = Date()
        val df = SimpleDateFormat("HH")
        val str = df.format(date)
        val a = str.toInt()
        if (a in 0..6) {
            return "凌晨好"
        }
        if (a in 7..12) {
            return "上午好"
        }
        if (a == 13) {
            return "中午好"
        }
        if (a in 14..18) {
            return "下午好"
        }
        return if (a in 19..24) {
            "晚上好"
        } else ""
    }

val aPm: String
    get() {
        val time = System.currentTimeMillis()
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = time
        val apm = mCalendar[Calendar.AM_PM]

        // apm = 0 表示上午，apm = 1 表示下午。
        return if (apm == 0) "上午好" else "下午好"
    }

fun calculateTime(dateDiff: Long): String {
    return if (dateDiff < 0) {
        "输入的时间不对"
    } else {
        val dateTemp1 = dateDiff / 1000 // 秒
        val dateTemp2 = dateTemp1 / 60 // 分钟
        val dateTemp3 = dateTemp2 / 60 // 小时
        val dateTemp4 = dateTemp3 / 24 // 天数
        val dateTemp5 = dateTemp4 / 30 // 月数
        val dateTemp6 = dateTemp5 / 12 // 年数
        when {
            dateTemp6 > 0 -> "好久"
            dateTemp5 > 0 -> dateTemp5.toString() + "个月"
            dateTemp4 > 0 -> dateTemp4.toString() + "天"
            dateTemp3 > 0 -> dateTemp3.toString() + "小时"
            dateTemp2 > 0 -> dateTemp2.toString() + "分钟"
            dateTemp1 > 0 -> "刚刚"
            else -> "未知"
        }
    }
}

val today: String
    get() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR].toString()
        val month = (calendar[Calendar.MONTH] + 1).toString()
        val day = calendar[Calendar.DAY_OF_MONTH].toString()
        return "$year-$month-$day"
    }

val currentDate: IntArray
    get() {
        val calendar = Calendar.getInstance()
        return intArrayOf(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH]
        )
    }