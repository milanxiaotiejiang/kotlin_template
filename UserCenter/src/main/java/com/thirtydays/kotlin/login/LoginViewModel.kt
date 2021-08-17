package com.thirtydays.kotlin.login

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seabreeze.robot.base.ext.coroutine.ExceptionHandler
import com.seabreeze.robot.base.ext.coroutine.launchFlow
import com.seabreeze.robot.base.ext.coroutine.launchUI
import com.seabreeze.robot.base.ext.foundation.dcEither
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.data.DataSettings
import com.seabreeze.robot.data.net.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/9
 * @description : TODO
 * </pre>
 */
class LoginViewModel : BaseViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    fun checkLoginEnable() {
        _isLoginEnable.value = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
    }

    val isLoginSuccess = MutableLiveData<Boolean>()

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun login() = launchUI {
        launchFlow {
            DataSettings.username = username.value ?: ""
            DataSettings.password = password.value ?: ""

            DataRepository.INSTANCE.userLogin(
                mapOf(
                    Pair("username", DataSettings.username),
                    Pair("password", DataSettings.password)
                )
            )
        }
            .flowOn(Dispatchers.IO)
            .onStart { uiLiveEvent.showLoadingProgressBarEvent.call() }
            .catch { uiLiveEvent.errorEvent.value = ExceptionHandler.handleException(it) }
            .onCompletion { uiLiveEvent.dismissLoadingProgressBarEvent.call() }
            .collect { result ->
                result.dcEither().fold({
                    DataSettings.saveAccount(it)
                    isLoginSuccess.value = true
                }, {
                    uiLiveEvent.errorEvent.postValue(it)
                })
            }
    }

    interface Handlers {

        fun onUsernameAfterTextChanged(editable: Editable)

        fun onPasswordAfterTextChanged(editable: Editable)

        fun onLoginClick(view: View)

    }
}