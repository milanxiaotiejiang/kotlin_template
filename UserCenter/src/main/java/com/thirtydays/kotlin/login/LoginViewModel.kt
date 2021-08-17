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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

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

    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    val codeText = MutableLiveData<String>()
    private val _isCodeEnabled = MutableLiveData<Boolean>()
    val isCodeEnabled: LiveData<Boolean> = _isCodeEnabled

    init {
        username.value = DataSettings.username
        password.value = DataSettings.password
        codeText.value = "倒计时"
        _isCodeEnabled.value = true
    }


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

    private val codeTimes: Long = 59

    fun onCode() {
        Observable.intervalRange(0, codeTimes + 2, 0, 1, TimeUnit.SECONDS)
            .take(codeTimes + 1)
            .map { aLong: Long -> codeTimes - aLong }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _isCodeEnabled.value = false
            }
            .subscribe(object : io.reactivex.Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onNext(aLong: Long) {
                    codeText.postValue("$aLong s后重新获取")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    _isCodeEnabled.value = true
                    codeText.postValue("重新获取")
                }
            })
    }

    /**
     * 添加订阅
     */
    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * 取消所有订阅
     */
    fun clearDisposable() {
        mCompositeDisposable.clear()
    }

    interface Handlers {

        fun onUsernameAfterTextChanged(editable: Editable)

        fun onPasswordAfterTextChanged(editable: Editable)

        fun onLoginClick(view: View)

        fun onRegisterClick(view: View)

        fun onCodeClick(view: View)

        fun onQqClick(view: View)

        fun onWxClick(view: View)
    }
}