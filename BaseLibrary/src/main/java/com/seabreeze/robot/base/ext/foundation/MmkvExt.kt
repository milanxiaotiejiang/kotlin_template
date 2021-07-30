package com.seabreeze.robot.base.ext.foundation

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
@Suppress("UNCHECKED_CAST")
class Mmkv<T>(val name: String, val default: T) : ReadWriteProperty<Any?, T> {

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = findPreference(name)

    private fun findPreference(key: String): T {
        return when (default) {
            is Double -> mmkv.decodeDouble(key, default)
            is Boolean -> mmkv.decodeBool(key, default)
            is Long -> mmkv.decodeLong(key, default)
            is Int -> mmkv.decodeInt(key, default)
            is Float -> mmkv.decodeFloat(key, default)
            is String -> mmkv.decodeString(key, default)
            is ByteArray -> mmkv.decodeBytes(key, default)
            else -> throw IllegalArgumentException("Unsupported type")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun putPreference(key: String, value: T) {
        mmkv.apply {
            when (value) {
                is Double -> encode(key, value)
                is Boolean -> encode(key, value)
                is Long -> encode(key, value)
                is Int -> encode(key, value)
                is Float -> encode(key, value)
                is String -> encode(key, value)
                is ByteArray -> encode(key, value)
            }
        }
    }

}