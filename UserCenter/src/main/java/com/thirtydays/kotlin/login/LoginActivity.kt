package com.thirtydays.kotlin.login

import android.text.Editable
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.seabreeze.robot.data.DataSettings
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.databinding.ActivityLoginBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/9
 * @description : TODO
 * </pre>
 */
@Route(path = RouterPath.UserCenter.PATH_APP_LOGIN)
class LoginActivity :
    BaseVmActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login),
    LoginViewModel.Handlers {

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
            handlers = this@LoginActivity
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }
    }

    override fun initViewModelActions() {
        observe(mViewModel.isLoginSuccess) {
            it.yes {
                startMain()
                this@LoginActivity.finish()
            }
        }
    }

    override fun initData() {
        mViewModel.username.postValue(DataSettings.username)
        mViewModel.password.postValue(DataSettings.password)
    }

    override fun onUsernameAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onPasswordAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onLoginClick(view: View) {
        mViewModel.login()
    }
}