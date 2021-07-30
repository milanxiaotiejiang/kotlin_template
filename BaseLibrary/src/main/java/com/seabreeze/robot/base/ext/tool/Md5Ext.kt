@file:JvmName("MD5")

package com.seabreeze.robot.base.ext.tool

import java.security.MessageDigest
import java.util.*

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   :
 * </pre>
 */
/**
 * @param plainText 明文
 * @return 32位密文md5
 */
fun String.encryption(): String {
    var reMd5 = ""
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(toByteArray())
        val b = md.digest()
        var i: Int
        val buf = StringBuilder("")
        for (value in b) {
            i = value.toInt()
            if (i < 0) i += 256
            if (i < 16) buf.append("0")
            buf.append(Integer.toHexString(i))
        }
        reMd5 = buf.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return reMd5.toUpperCase(Locale.ROOT)
}