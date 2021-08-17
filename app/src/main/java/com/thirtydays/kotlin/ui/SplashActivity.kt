package com.thirtydays.kotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.seabreeze.robot.base.ext.foundation.otherwise
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.router.startLogin
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.databinding.ActivitySplashBinding
import com.thirtydays.kotlin.ktx.DataSettings.user_id
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/5
 * @description : 闪屏页面
 * </pre>
 */
class SplashActivity :
    BaseVmActivity<SplashViewModel, ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onInitDataBinding() {
        with(mViewModel) {
            navigateToPage()
            isNavigateToMainActivity.observe(this@SplashActivity, Observer {
                it
                    .yes { startMain() }
                    .otherwise { startLogin() }
                finish()
            })
        }
    }

    override fun initData() {

    }
}

class SplashViewModel : BaseViewModel() {

    private val _isNavigateToMainActivity = MutableLiveData<Boolean>()
    var isNavigateToMainActivity: LiveData<Boolean> = _isNavigateToMainActivity

    fun navigateToPage() =
        viewModelScope.launch {
            delay(2000)
            _isNavigateToMainActivity.value = user_id != -1
        }
}