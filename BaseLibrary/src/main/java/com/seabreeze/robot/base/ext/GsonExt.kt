@file:JvmName("GsonUtil")

package com.seabreeze.robot.base.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser.parseString
import com.google.gson.reflect.TypeToken
import com.seabreeze.robot.base.ext.GsonExt.gson

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object GsonExt {

    val gson: Gson by lazy {
        GsonBuilder()
            //序列化null
//            .serializeNulls()
            // 设置日期时间格式，另有2个重载方法
            // 在序列化和反序化时均生效
//            .setDateFormat("yyyy-MM-dd")
            // 禁此序列化内部类
//            .disableInnerClassSerialization()
            //生成不可执行的Json（多了 )]}' 这4个字符）
//            .generateNonExecutableJson()
            //禁止转义html标签
//            .disableHtmlEscaping()
            //格式化输出
//            .setPrettyPrinting()
            .create()
    }
}

fun Any.gToJson(): String = gson.toJson(this)

inline fun <reified T> String.gToBean(): T? =
    gson.fromJson(this, object : TypeToken<T>() {}.type)

inline fun <reified T> String.gToList(): List<T> {
    val list = mutableListOf<T>()
    parseString(this)
        .asJsonArray
        .forEach {
            list.add(gson.fromJson(it, object : TypeToken<T>() {}.type))
        }
    return list
}