package com.seabreeze.robot.base.ext.foundation

import com.seabreeze.robot.base.ext.tool.postEvent
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.base.model.TokenInvalidEvent
import com.seabreeze.robot.base.router.startMain
import retrofit2.HttpException

/**
 * User: milan
 * Time: 2020/4/20 19:25
 * Des:
 */

sealed class BaseThrowable(
    val code: String? = null,
    message: String? = null,
    cause: Throwable? = null
) :
    Throwable(message, cause) {

    class ExternalThrowable(throwable: Throwable) :
        BaseThrowable(cause = throwable)

    class InsideThrowable(
        val errorCode: String,
        val errorMessage: String
    ) : BaseThrowable(code = errorCode, message = errorMessage)

    fun isExternal() = this is ExternalThrowable
    fun isInside() = this is InsideThrowable
}

fun BaseThrowable.onError() {
    when {
        isExternal() -> {
            val externalThrowable = this as BaseThrowable.ExternalThrowable
            externalThrowable.cause?.apply {
                message?.apply {
                    toast { this }
                }
                if (this is HttpException) {
                    when (code()) {
                        401 -> {
                            startMain(true)
                            postEvent(TokenInvalidEvent())
                        }
                    }
                }
            }
        }
        isInside() -> {
            val insideThrowable = this as BaseThrowable.InsideThrowable
            toast { insideThrowable.errorMessage }
            if (insideThrowable.errorCode == "000000") {
                startMain(true)
                postEvent(TokenInvalidEvent())
            }
        }
    }
}