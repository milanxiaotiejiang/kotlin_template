package com.seabreeze.robot.base.ext.coroutine

import android.hardware.Camera
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * User: milan
 * Time: 2020/4/9 16:22
 * Des:
 */
fun ViewModel.launchUI(block: Block) =
    viewModelScope.launch {
        block()
    }

fun <T> launchFlow(block: suspend () -> T): Flow<T> =
    flow {
        emit(block())
    }

/**
 * 处理逻辑
 *
 * @param block 请求块
 * @param success 成功回调
 * @param error 失败回调
 * @param complete 完成回调（成功或者失败都会回调）
 */
suspend fun <T> handle(
    block: suspend CoroutineScope.() -> T,
    success: suspend CoroutineScope.(T) -> Unit,
    error: ErrorCallback,
    complete: CommonCallback
) =
    coroutineScope {
        try {
            success(block())
        } catch (throwable: Throwable) {
            error(ExceptionHandler.handleException(throwable))
        } finally {
            complete()
        }
    }

suspend fun <T : Any> coroutineRequest(block: suspend () -> Either<T, Throwable>): Either<T, Throwable> =
    try {
        block()
    } catch (e: Exception) {
        Either.right(e)
    }

internal typealias Block = suspend CoroutineScope.() -> Unit

private typealias CommonCallback = suspend CoroutineScope.() -> Unit
private typealias ErrorCallback = suspend CoroutineScope.(BaseThrowable) -> Unit