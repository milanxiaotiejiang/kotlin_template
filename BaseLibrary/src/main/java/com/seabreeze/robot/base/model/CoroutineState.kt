package com.seabreeze.robot.base.model

import com.seabreeze.robot.base.framework.mvvm.BaseViewState

sealed class CoroutineState : BaseViewState {
    object Loading : CoroutineState()
    object Finish : CoroutineState()
    object Error : CoroutineState()

    fun isLoading() = this is Loading
    fun isError() = this is Error
    fun isFinish() = this is Finish

}