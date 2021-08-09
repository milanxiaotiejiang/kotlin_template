package com.seabreeze.robot.base.ext.coroutine

import android.util.MalformedJsonException
import com.google.gson.JsonParseException
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLException

object ExceptionHandler {

    fun handleException(throwable: Throwable): BaseThrowable.ExternalThrowable =
        when (throwable) {
            is JsonParseException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "JsonParseException",
                    throwable = throwable
                )

            is JSONException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "JSONException",
                    throwable = throwable
                )

            is ParseException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "ParseException",
                    throwable = throwable
                )

            is MalformedJsonException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "MalformedJsonException",
                    throwable = throwable
                )

            is ConnectException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "ConnectException",
                    throwable = throwable
                )

            is HttpException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = throwable.message(),
                    throwable = throwable
                )

            is SSLException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "SSLException",
                    throwable = throwable
                )

            is SocketTimeoutException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "SocketTimeoutException",
                    throwable = throwable
                )

            is UnknownHostException ->
                BaseThrowable.ExternalThrowable(
                    errorMessage = "UnknownHostException",
                    throwable = throwable
                )

            else ->
                BaseThrowable.ExternalThrowable(throwable = throwable)
        }

}