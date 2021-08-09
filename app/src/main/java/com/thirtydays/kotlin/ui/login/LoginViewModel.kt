package com.thirtydays.kotlin.ui.login

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seabreeze.robot.base.ext.coroutine.launchFlow
import com.seabreeze.robot.base.ext.coroutine.launchUI
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.data.net.DataRepository
import com.seabreeze.robot.data.net.bean.request.GithubLoginRequest
import com.thirtydays.kotlin.BuildConfig
import com.thirtydays.kotlin.ktx.DataSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import me.yokeyword.fragmentation.helper.ExceptionHandler

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

    val isLoginSuccess = MutableLiveData<Boolean>()

    fun checkLoginEnable() {
        _isLoginEnable.value = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
    }


    @ExperimentalCoroutinesApi
    @FlowPreview
    fun login() =
        launch(true) {
            launchFlow {
                username.value = DataSettings.username
                password.value = DataSettings.password


                DataRepository.INSTANCE.authorizations(
                    GithubLoginRequest.generate(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.GITHUB_CLIENT_ID,
                        BuildConfig.GITHUB_CLIENT_SECRET
                    )
                )
            }
                .flatMapMerge {
                    launchFlow {
                        DataRepository.INSTANCE.fetchUserInfo()
                    }
                }
                .flowOn(Dispatchers.IO)
                .onStart { uiLiveEvent.showLoadingProgressBarEvent.call() }
                .catch {
                    val responseThrowable = ExceptionHandler.handleException(it)
                    uiLiveEvent.showSnackbarEvent.value =
                        "${responseThrowable.errorCode}:${responseThrowable.errorMessage}"
                }
                .onCompletion { uiLiveEvent.dismissLoadingProgressBarEvent.call() }
                .collect {
                    repository.run {
                        cacheUserId(it.id)
                        cacheName(it.login)
                        cacheAvatarUrl(it.avatarUrl)
                    }
                    isLoginSuccess.value = true
                }
        }

    interface Handlers {

        fun onUsernameAfterTextChanged(editable: Editable)

        fun onPasswordAfterTextChanged(editable: Editable)

        fun onLoginClick(view: View)

    }
}