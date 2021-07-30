package com.seabreeze.robot.base.framework.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seabreeze.robot.base.ext.coroutine.Block
import com.seabreeze.robot.base.ext.coroutine.launchUI
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.model.CoroutineState

abstract class BaseViewModel : ViewModel() {
    /**
     * 协程状态管理
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }
    val error = MutableLiveData<BaseThrowable>()

    fun launch(show: Boolean = false, block: Block) =
        launchUI {
            try {
                if (show) statusLiveData.postValue(CoroutineState.Loading)
                block()
                if (show) statusLiveData.postValue(CoroutineState.Finish)
            } catch (e: Exception) {
                if (show) statusLiveData.postValue(CoroutineState.Error)
                error.postValue(BaseThrowable.ExternalThrowable(e))
            }
        }

}