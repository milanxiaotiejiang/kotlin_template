package com.seabreeze.robot.base.ext

import androidx.lifecycle.viewModelScope
import com.seabreeze.robot.base.model.Either
import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.base.vm.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 协程相关api
 * </pre>
 */
fun <T : BaseRepository> BaseViewModel<T>.launchUI(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch {
        block()
    }

suspend fun <T : Any> coroutineRequest(block: suspend () -> Either<T, Throwable>): Either<T, Throwable> =
    try {
        block()
    } catch (e: Exception) {
        Either.right(e)
    }