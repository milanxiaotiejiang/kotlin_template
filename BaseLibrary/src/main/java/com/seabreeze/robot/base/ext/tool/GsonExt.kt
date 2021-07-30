@file:JvmName("GsonUtil")

package com.seabreeze.robot.base.ext.tool

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser.parseString
import com.google.gson.reflect.TypeToken
import com.seabreeze.robot.base.ext.tool.GsonExt.gson

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object GsonExt {

    val gson: Gson by lazy {
        GsonBuilder().create()
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