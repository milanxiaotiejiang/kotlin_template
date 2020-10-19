package com.thirtydays.kotlin.wxapi

import android.content.Intent
import android.os.Bundle
import com.seabreeze.robot.base.ext.postEvent
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.seabreeze.robot.third.ext.WxPay.createWx
import com.seabreeze.robot.third.model.WxPayEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/18
 * @description : TODO
 * </pre>
 */
class WXPayEntryActivity : RxAppCompatActivity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createWx()?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        createWx()?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        finish()
    }

    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> toast("支付成功")
                BaseResp.ErrCode.ERR_USER_CANCEL -> toast("支付取消")
                else -> toast("支付错误 ${resp.errStr}")
            }
            postEvent(WxPayEvent(resp))
        }
        finish()
    }
}