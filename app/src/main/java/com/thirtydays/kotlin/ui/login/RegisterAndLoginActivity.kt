package com.thirtydays.kotlin.ui.login

import android.text.Editable
import android.view.View
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.databinding.ActivityRegisterAndLoginBinding
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
class RegisterAndLoginActivity :
    BaseVmActivity<LoginViewModel, ActivityRegisterAndLoginBinding>(R.layout.activity_register_and_login),
    LoginViewModel.Handlers {

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
            handlers = this@RegisterAndLoginActivity
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }
    }

    override fun initViewModelActions() {

    }

    override fun initData() {
        observe(mViewModel.isLoginSuccess) {
            it.yes {
                startMain()
                this@RegisterAndLoginActivity.finish()
            }
        }
    }

    override fun onUsernameAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onPasswordAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onLoginClick(view: View) {
        mViewModel.login()
    }
}