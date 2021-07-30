package com.seabreeze.robot.base.ext.foundation

import java.util.*

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object UUIDGenerator {
    val uuid: String
        get() = UUID.randomUUID().toString()

    //获得指定数量的UUID
    fun getUUID(number: Int): Array<String?>? {
        if (number < 1) {
            return null
        }
        val array = arrayOfNulls<String>(number)
        for (i in 0 until number) {
            array[i] = uuid
        }
        return array
    }
}