package com.seabreeze.robot.base.framework.mvvm

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seabreeze.robot.base.ext.coroutine.ExceptionHandler
import com.seabreeze.robot.base.ext.coroutine.SingleLiveEvent
import com.seabreeze.robot.base.ext.coroutine.launchUI
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.dcEither
import com.seabreeze.robot.base.model.BaseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

private typealias CompleteCallback = suspend CoroutineScope.() -> Unit
private typealias ErrorCallback = suspend CoroutineScope.(BaseThrowable) -> Unit

abstract class BaseViewModel : ViewModel() {

    // 是否显示加载中页面
    private val _isShowLoadingView = MutableLiveData<Boolean>()
    val isShowLoadingView: LiveData<Boolean> = _isShowLoadingView

    // 是否显示失败页面
    private val _isShowErrorView = MutableLiveData<Boolean>()
    val isShowErrorView: LiveData<Boolean> = _isShowErrorView

    // UI事件
    val uiLiveEvent by lazy { UILiveEvent() }

    fun <T> launch(
        uiState: UIState = UIState(),
        block: suspend CoroutineScope.() -> BaseResult<T>,
        success: (suspend CoroutineScope.(T) -> Unit)? = null,
        error: (ErrorCallback)? = null,
        complete: (CompleteCallback)? = null
    ) =
        with(uiState) {
            if (isShowLoadingProgressBar) uiLiveEvent.showLoadingProgressBarEvent.call()
            if (isShowLoadingView) _isShowLoadingView.value = true
            if (isShowErrorView) _isShowErrorView.value = false
            launchUI {
                handle(
                    block = withContext(Dispatchers.IO) {
                        block
                    },
                    success = { s ->
                        withContext(Dispatchers.Main) {
                            s.dcEither().fold({ t ->
                                success?.invoke(this, t)
                            }, { e ->
                                if (isShowErrorToast) uiLiveEvent.errorEvent.postValue(e)
                                if (isShowErrorView) _isShowErrorView.value = true
                                error?.invoke(this, e)
                            })

                        }
                    },
                    error = { e ->
                        withContext(Dispatchers.Main) {
                            if (isShowErrorToast) uiLiveEvent.errorEvent.postValue(e)
                            if (isShowErrorView) _isShowErrorView.value = true
                            error?.invoke(this, e)
                        }
                    },
                    complete = {
                        withContext(Dispatchers.Main) {
                            if (isShowLoadingProgressBar) uiLiveEvent.dismissLoadingProgressBarEvent.call()
                            if (isShowLoadingView) _isShowLoadingView.value = false
                            complete?.invoke(this)
                        }
                    }
                )
            }
        }

    /**
     * 处理逻辑
     *
     * @param block 请求块
     * @param success 成功回调
     * @param error 失败回调
     * @param complete 完成回调（成功或者失败都会回调）
     */
    private suspend fun <T> handle(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: ErrorCallback,
        complete: CompleteCallback
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

    inner class UILiveEvent {
        val errorEvent by lazy { SingleLiveEvent<BaseThrowable>() }
        val showLoadingProgressBarEvent by lazy { SingleLiveEvent<Boolean>() }
        val dismissLoadingProgressBarEvent by lazy { SingleLiveEvent<Boolean>() }
    }


    interface Handlers {
        fun onRetryClick(view: View) {

        }
    }

}

/**
 * User: milan
 * Time: 2021/8/9 16:10
 * @param isShowLoadingProgressBar 是否显示加载中ProgressBar
 * @param isShowLoadingView 是否显示加载中页面
 * @param isShowErrorToast 是否弹出错误Toast
 * @param isShowErrorView 是否显示错误页面
 */
data class UIState(
    val isShowLoadingProgressBar: Boolean = false,
    val isShowLoadingView: Boolean = false,
    val isShowErrorView: Boolean = false,
    val isShowErrorToast: Boolean = false
)