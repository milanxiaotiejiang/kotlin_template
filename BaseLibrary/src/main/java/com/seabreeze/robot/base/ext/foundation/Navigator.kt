package com.seabreeze.robot.base.ext.foundation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * User: milan
 * Time: 2020/3/24 17:04
 * Des:
 */
inline val Context.ctx: Context
    get() = this

inline val Activity.act: Activity
    get() = this

inline val Fragment.fra: Fragment
    get() = this

inline fun <reified T : Activity> Context.pop(vararg params: Pair<String, Any?>) =
    ctx.startActivity(createIntent(this, T::class.java, params))

inline fun <reified T : Activity> Activity.pop(vararg params: Pair<String, Any?>) =
    act.startActivity(createIntent(this, T::class.java, params))

inline fun <reified T : Activity> Activity.popResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) = act.startActivityForResult(createIntent(this, T::class.java, params), requestCode)

inline fun <reified T : Activity> Fragment.pop(vararg params: Pair<String, Any?>) =
    fra.startActivity(fra.activity?.let {
        createIntent(it, T::class.java, params)
    })

inline fun <reified T : Activity> Fragment.popResult(
    requestCode: Int, vararg params: Pair<String, Any?>
) = fra.startActivityForResult(fra.activity?.let {
    createIntent(it, T::class.java, params)
}, requestCode)


fun <T> createIntent(
    ctx: Context,
    clazz: Class<out T>,
    params: Array<out Pair<String, Any?>>
): Intent {
    val intent = Intent(ctx, clazz)
    if (params.isNotEmpty()) fillIntentArguments(intent, params)
    return intent
}

private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
    params.forEach {
        when (val value = it.second) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw IntentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw IntentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

open class IntentException(message: String = "") : RuntimeException(message)

fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }
fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

fun Intent.clearWhenTaskReset(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT) }

fun Intent.newDocument(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT) }

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
    createIntent(this, T::class.java, params)