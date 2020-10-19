package com.seabreeze.robot.third.ui

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ext.gToBean
import com.seabreeze.robot.base.ext.registerEvent
import com.seabreeze.robot.base.ext.unregisterEvent
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.ui.activity.BaseMvvmActivity
import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.base.vm.BaseViewModel
import com.seabreeze.robot.data.common.Common
import com.seabreeze.robot.third.R
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/9
 * @description : TODO
 * </pre>
 */

class LoginViewModel : BaseViewModel<LoginRepository>() {
    override fun createRepository() = LoginRepository()
}

class LoginRepository : BaseRepository()

@Route(path = RouterPath.UserCenter.PATH_APP_LOGIN)
class LoginActivity : BaseMvvmActivity<LoginRepository, LoginViewModel>() {

    private lateinit var inputMethodManager: InputMethodManager

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
            onError(throwable = Throwable(error?.errorMessage))
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
            onError(throwable = Throwable(uiError.errorMessage))
        })
    }

    override fun setImmersionBar() {
        ImmersionBar.with(this)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .statusBarColor(android.R.color.white)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    override fun createViewModel() = ViewModelProvider(this)[LoginViewModel::class.java]

    override fun getLayoutId() = R.layout.activity_login

    override fun initViewModel() {
        mViewModel.run {

        }
    }

    override fun initData() {
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val tip = "我已经阅读并同意使用条款和隐私政策"
        autoLinkStyleTextView.text = tip
        autoLinkStyleTextView.setDefaultTextValue("使用条款和隐私政策")
        autoLinkStyleTextView.addClickCallBack {
        }

        addDisposable(loginQQBtn.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                loginQQ(iuiListener)
            }
        )

        addDisposable(loginWechatBtn.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                loginWx()
            }
        )

        addDisposable(tvCode.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                countDown()
            })

        registerEvent()
    }


    override fun onDestroy() {
        unregisterEvent()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iuiListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultEvent(event: WxLoginEvent) {
        if (event.ok)
            event.bean.fold({ wxResult ->
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

    private val codeTimes: Long = 59

    private fun countDown() {
        Observable.intervalRange(0, codeTimes + 2, 0, 1, TimeUnit.SECONDS)
            .take(codeTimes + 1)
            .map { aLong: Long -> codeTimes - aLong }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                tvCode.isEnabled = false
            }
            .subscribe(object : io.reactivex.Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onNext(aLong: Long) {
                    tvCode.text = "$aLong s后重新获取"
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    tvCode.isEnabled = true
                    tvCode.text = "重新获取"
                }
            })
    }

    private fun hideSoftInputFromWindow() {
        currentFocus?.apply {
            windowToken?.run {
                inputMethodManager.hideSoftInputFromWindow(this, 0)
            }
        }
    }
}