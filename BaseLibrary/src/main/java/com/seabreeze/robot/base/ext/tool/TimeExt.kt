@file:JvmName("TimeUtil")

package com.seabreeze.robot.base.ext.tool

import com.elvishew.xlog.XLog
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   : 时间日期工具类 (主要负责格式转换)
 * </pre>
 */
const val FORMAT_YMDHM = "yyyy-MM-dd HH:mm"
const val FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss"
const val FORMAT_YMD = "yyyy-MM-dd"
const val FORMAT_YMD_CHINESE = "yyyy年MM月dd日"
const val FORMAT_HM = "HH:mm"
const val FORMAT_HMS = "HH:mm:ss"
private const val TAG = "TimeUtil"

/**服务器返回的时间格式**/
internal var serverFormat = "yyyy-MM-dd HH:mm:ss"

/**默认时区，与主机的设置一致**/
internal var defTimeZone: TimeZone = TimeZone.getDefault()

/**获取当前年份**/
val currentYear: Int
    get() = Calendar.getInstance().get(Calendar.YEAR)


//fun currentYear(timeZone: TimeZone = defTimeZone)= Calendar.getInstance(timeZone).get(Calendar.YEAR)

/**获取当前月份**/
val currentMonth
    get() = Calendar.getInstance().get(Calendar.MONTH) + 1


/**获取当前日**/
val currentDay: Int
    get() = Calendar.getInstance().get(Calendar.DATE)


/**获取当前时间戳**/
val currentTimeMillis: Long
    get() = System.currentTimeMillis()

/**
 * 获取当前日期，默认格式为"yyyy-MM-dd"
 * @param pattern:输出的格式
 */
@JvmOverloads
fun formatCurrentDate(pattern: String = FORMAT_YMD) = getTimeFormatStr(pattern)

/**
 * 获取当前时间，默认格式为"yyyy-MM-dd HH:mm"
 * @param pattern:输出的格式
 */
@JvmOverloads
fun formatCurrentDateTime(pattern: String = FORMAT_YMDHM) = getTimeFormatStr(pattern)

/**
 * 获取当前时间，格式为"HH:mm"
 * @param pattern:输出的格式
 */
@JvmOverloads
fun formatCurrentTime(pattern: String = FORMAT_HM) = getTimeFormatStr(pattern)

/**
 * 将服务器时间格式转换为年
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeY(target: String = "yyyy", source: String = serverFormat) =
    formatTimeWithPattern(target, source)


/**
 * 将服务器时间格式转换为月，保持两位，不足两位时在前面补上0
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeMM(target: String = "MM", source: String = serverFormat) =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为月
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeM(target: String = "MM", source: String = serverFormat) =
    formatTimeWithPattern(target, source).removeFirstZero()

/**
 * 将服务器时间格式转换为年月
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeYM(target: String = "yyyy-MM", source: String = serverFormat) =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为日，保持两位，不足两位时在前面补上0
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeDD(target: String = "dd", source: String = serverFormat) =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为日
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeD(target: String = "dd", source: String = serverFormat) =
    formatTimeDD(target, source).removeFirstZero()


/**
 * 将服务器时间格式转换为年月日
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeYMD(target: String = FORMAT_YMD, source: String = serverFormat): String =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为年月日（带汉字）
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeYMDChinese(
    target: String = FORMAT_YMD_CHINESE,
    source: String = serverFormat
): String =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为时分
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeHM(target: String = FORMAT_HM, source: String = serverFormat): String =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为时分秒
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeHMS(target: String = FORMAT_HMS, source: String = serverFormat): String =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为年月日时分
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeYMDHM(target: String = FORMAT_YMDHM, source: String = serverFormat): String =
    formatTimeWithPattern(target, source)

/**
 * 将服务器时间格式转换为年月日时分秒
 * @param target:输出的格式
 * @param source:源格式
 */
@JvmOverloads
fun String.formatTimeYMDHMS(target: String = FORMAT_YMDHMS, source: String = serverFormat): String =
    formatTimeWithPattern(target, source)

/**
 * 根据服务器的时间判断星期
 * @return:0表示星期日，1~6依次表示星期一到星期六
 */
@JvmOverloads
fun String.formatTimeWeek(timeZone: TimeZone = defTimeZone): Int {
    val formatter = SimpleDateFormat(FORMAT_YMD, Locale.getDefault())
    val cal = Calendar.getInstance(timeZone)
    return try {
        val parse = formatter.parse(this)
        if (parse != null) {
            cal.time = parse
            cal.get(Calendar.DAY_OF_WEEK) - 1
        } else {
            XLog.e(TAG, "星期转换错误：$this")
            -1
        }
    } catch (e: ParseException) {
        XLog.e(TAG, "星期转换错误：$this")
        -1
    }
}

/**
 * @param target 转换后的时间格式
 * @param source 转换前的时间格式，一般以服务器为准
 * @return 失败是返回空字符串
 */
private fun String.formatTimeWithPattern(target: String, source: String = serverFormat): String {
    val formatter = SimpleDateFormat(source, Locale.getDefault())
    return try {
        val date = formatter.parse(this, ParsePosition(0))
        return if (date != null) {
            SimpleDateFormat(target, Locale.getDefault()).format(date)
        } else {
            ""
        }
    } catch (e: Exception) {
        XLog.e(TAG, "日期转换错误：$this")
        ""
//        App.instance.getString(R.string.empty_result)
    }
}

fun getTimeFormatStr(target: String, source: Any = Date()) =
    SimpleDateFormat(target, Locale.getDefault()).format(source) ?: ""

/**
 * 去除前面的0
 */
private fun String.removeFirstZero() = if (this.startsWith("0")) {
    replaceFirst("0", "")
} else this

internal var specialFormat = "yyyy MM dd HH mm ss"

/**
 * 格式串转时间戳
 */
fun String.formatTime(source: String = specialFormat) =
    SimpleDateFormat(source, Locale.getDefault()).parse(this, ParsePosition(0)).time