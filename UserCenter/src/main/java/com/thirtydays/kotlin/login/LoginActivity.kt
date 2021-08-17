package com.thirtydays.kotlin.login

import android.content.Intent
import android.text.Editable
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.pop
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.ext.tool.registerEvent
import com.seabreeze.robot.base.ext.tool.unregisterEvent
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.seabreeze.robot.data.common.Common
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.databinding.ActivityLoginBinding
import com.seabreeze.robot.third.ext.ThirdQQ
import com.seabreeze.robot.third.ext.loginQQ
import com.seabreeze.robot.third.ext.loginQQInfo
import com.seabreeze.robot.third.ext.loginWx
import com.seabreeze.robot.third.model.ThirdInfo
import com.seabreeze.robot.third.model.WxLoginEvent
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.thirtydays.kotlin.register.RegisterActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject

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

        registerEvent()
    }

    override fun onDestroy() {
        mViewModel.clearDisposable()
        unregisterEvent()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iuiListener);
        }
    }

    override fun onUsernameAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    override fun onPasswordAfterTextChanged(editable: Editable) = mViewModel.checkLoginEnable()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onLoginClick(view: View) {
        mViewModel.login()
    }

    override fun onRegisterClick(view: View) {
        pop<RegisterActivity>()
        finish()
    }

    override fun onCodeClick(view: View) {
        mViewModel.onCode()
    }

    private val iuiListener = object : IUiListener {
        override fun onComplete(response: Any?) {
            response?.let {
                if (it is JSONObject) {
                    it.toString().gToBean<ThirdQQ>()?.let { thirdQQ ->
                        loginQQUserInfo(thirdQQ)
                    }
                }
            }
        }

        override fun onError(error: UiError?) {
            onError(throwable = BaseThrowable.ExternalThrowable(Throwable(error?.errorMessage)))
        }

        override fun onCancel() {
            showToast("取消QQ登录")
        }

    }

    private fun loginQQUserInfo(thirdQQ: ThirdQQ) {
        loginQQInfo(thirdQQ, {
            val thirdQQInfo = it.thirdQQInfo
            thirdQQInfo?.let { info ->
                val thirdInfo = ThirdInfo(
                    thirdQQ.openid,
                    Common.THREE_TYPE_QQ,
                    info.nickname,
                    info.figureurl_qq
                )
                // TODO: 2020/9/29
            }

        }, { uiError ->
            onError(throwable = BaseThrowable.ExternalThrowable(Throwable(uiError.errorMessage)))
        })
    }

    override fun onQqClick(view: View) {
        loginQQ(iuiListener)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultEvent(event: WxLoginEvent) {
        event.bean?.fold({ wxResult ->
            wxResult.wxInfo?.let { wxInfo ->
                val thirdInfo = ThirdInfo(
                    wxResult.openid,
                    Common.THREE_TYPE_WX,
                    wxInfo.nickname,
                    wxInfo.headimgurl
                )
                // TODO: 2020/9/29
            }
        }, {
            onError(it)
        })
    }

    override fun onWxClick(view: View) {
        loginWx()
    }
}