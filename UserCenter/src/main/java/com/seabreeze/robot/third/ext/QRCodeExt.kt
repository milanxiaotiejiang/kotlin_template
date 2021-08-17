package com.seabreeze.robot.third.ext

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/18
 * @description : TODO
</pre> *
 */
/**
 * 创建二维码位图 (支持自定义配置和自定义样式)
 *
 * @param content          字符串内容
 * @param width            位图宽度,要求>=0(单位:px)
 * @param height           位图高度,要求>=0(单位:px)
 * @param character_set    字符集/字符转码格式 (支持格式:[com.google.zxing.common.CharacterSetECI])。传null时,zxing源码默认使用 "ISO-8859-1"
 * @param error_correction 容错级别 (支持级别:[com.google.zxing.qrcode.decoder.ErrorCorrectionLevel])。传null时,zxing源码默认使用 "L"
 * @param margin           空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
 * @param color_black      黑色色块的自定义颜色值
 * @param color_white      白色色块的自定义颜色值
 * @return
 */
@JvmOverloads
fun String.createQRCodeBitmap(
    width: Int = 100,
    height: Int = 100,
    character_set: String = "UTF-8",
    error_correction: ErrorCorrectionLevel = ErrorCorrectionLevel.H,
    margin: Int = 2,
    @ColorInt color_black: Int = Color.BLACK,
    @ColorInt color_white: Int = Color.WHITE
): Bitmap {
    /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
    val hints = Hashtable<EncodeHintType, Any>()
    hints[EncodeHintType.CHARACTER_SET] = character_set // 字符转码格式设置
    hints[EncodeHintType.ERROR_CORRECTION] = error_correction // 容错级别设置
    hints[EncodeHintType.MARGIN] = margin // 空白边距设置
    val bitMatrix =
        QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, width, height, hints)

    /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (bitMatrix[x, y]) {
                pixels[y * width + x] = color_black // 黑色色块像素设置
            } else {
                pixels[y * width + x] = color_white // 白色色块像素设置
            }
        }
    }
    /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象  */
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}