package com.thirtydays.kotlin.register

import android.text.Editable
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.pop
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.databinding.ActivityRegisterBinding
import com.thirtydays.kotlin.login.LoginActivity
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
@Route(path = RouterPath.UserCenter.PATH_APP_REGISTER)
class RegisterActivity :
    BaseVmActivity<RegisterViewModel, ActivityRegisterBinding>(R.layout.activity_register),
    RegisterViewModel.Handlers {

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
            handlers = this@RegisterActivity
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }
    }

    override fun initViewModelActions() {

    }

    override fun initData() {
        observe(mViewModel.isRegisterSuccess) {
            it.yes {
                startMain()
                this@RegisterActivity.finish()
            }
        }
    }

    override fun onUsernameAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onPasswordAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onRePasswordAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onLoginClick(view: View) {
        pop<LoginActivity>()
        finish()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onRegisterClick(view: View) {
        mViewModel.register()
    }
}