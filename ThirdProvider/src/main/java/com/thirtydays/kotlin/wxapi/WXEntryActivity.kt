package com.thirtydays.kotlin.wxapi

import android.content.Intent
import android.os.Bundle
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.tool.postEvent
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import com.seabreeze.robot.third.ext.WxPay.createWx
import com.seabreeze.robot.third.ext.loginWxRequest
import com.seabreeze.robot.third.model.WxLoginEvent
import com.seabreeze.robot.third.model.WxShareEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : BaseActivity(), IWXAPIEventHandler {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createWx()?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        createWx()?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        finish()
    }

    override fun onResp(resp: BaseResp) {

        when (resp.type) {
            ConstantsAPI.COMMAND_SENDAUTH -> {
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> XLog.e("token获取成功")
                    else -> {
                        XLog.e("token获取 ${resp.errStr}")
                        finish()
                    }
                }
                val authResp = resp as SendAuth.Resp
                loginWxRequest(authResp.code) {
                    postEvent(WxLoginEvent(it))
                    finish()
                }
            }
            ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> {
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> toast("分享成功")
                    BaseResp.ErrCode.ERR_USER_CANCEL -> toast("分享取消")
                    else -> toast("分享错误 ${resp.errStr}")
                }
                postEvent(WxShareEvent(resp))
                finish()
            }
            else -> {
                finish()
            }
        }

    }
}