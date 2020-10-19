package com.seabreeze.robot.third.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.seabreeze.robot.base.ext.registerEvent
import com.seabreeze.robot.base.ext.unregisterEvent
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.seabreeze.robot.third.R
import com.seabreeze.robot.third.ext.ALI_PAY_SUCCESSFUL
import com.seabreeze.robot.third.ext.payAli
import com.seabreeze.robot.third.ext.payWx
import com.seabreeze.robot.third.model.Recharge
import com.seabreeze.robot.third.model.WxPayEvent
import com.tencent.mm.opensdk.modelbase.BaseResp
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * User: milan
 * Time: 2020/9/18 23:52
 * Des:
 */
@Route(path = RouterPath.UserCenter.PATH_APP_PAY)
class PayActivity : RxAppCompatActivity() {

    private var payAliDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        //此处假设从服务端获取到支付数据，这里只展示方法的调用
        val recharge = Recharge()

        findViewById<View>(R.id.wxPayBtn).setOnClickListener {
            recharge.wxbody?.run {
                payWx(this)
            }

        }
        findViewById<View>(R.id.aliPayBtn).setOnClickListener {
            recharge.alibody?.apply {
                payAliDisposable = payAli(this) {
                    when (it.resultStatus) {
                        ALI_PAY_SUCCESSFUL -> paySuccess()
                        else -> "其它支付错误"
                    }
                }
            }
        }
        registerEvent()
    }

    private fun paySuccess() {
//        setResult(Activity.RESULT_OK)
//        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterEvent()
        payAliDisposable?.apply {
            if (!isDisposed)
                dispose()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultEvent(event: WxPayEvent) {
        if (event.ok)
            when (event.bean.errCode) {
                BaseResp.ErrCode.ERR_OK -> paySuccess()
                else -> {
                }
            }
    }
}