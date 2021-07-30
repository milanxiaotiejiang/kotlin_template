package com.thirtydays.kotlin.ui

import com.seabreeze.robot.base.ext.execute
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.seabreeze.robot.data.DataSettings.welcome
import com.thirtydays.kotlin.databinding.ActivitySplashBinding
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/5
 * @description : 闪屏页面
 * </pre>
 */
class SplashActivity : BaseMvpActivity<SplashPresenter, ActivitySplashBinding>() {

    override fun initData() {

        addDisposable(
            Flowable.just(welcome)
                .delay(2, TimeUnit.SECONDS)
                .execute(this)
                .subscribe {
                    startMain()
                    finish()
                })
    }
}

class SplashPresenter : BasePresenter<SplashActivity>()