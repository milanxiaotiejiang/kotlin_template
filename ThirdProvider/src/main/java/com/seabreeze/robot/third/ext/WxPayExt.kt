package com.seabreeze.robot.third.ext

import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.seabreeze.robot.third.ext.WxPay.createWx

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/14
 * @description : https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_4
 * </pre>
 */
const val WX_APP_ID: String = "wx9ec0bb60c23da99b"
const val WX_APP_SECRET: String = "adc17afb1971b2de44cd42e915edfc51"

object WxPay {
    private var wxAPI: IWXAPI? = null
    fun RxAppCompatActivity.createWx(): IWXAPI? {
        if (wxAPI != null) {
            return wxAPI!!
        }
        val createWXAPI = WXAPIFactory.createWXAPI(this, WX_APP_ID, false)
        return if (createWXAPI.isWXAppInstalled) {
            wxAPI = createWXAPI
            createWXAPI
        } else {
            toast { "微信未安装" }
            null
        }
    }
}

fun RxAppCompatActivity.payWx(wxbody: Wxbody) {
    val request = PayReq()
    request.appId = wxbody.appId
    request.partnerId = wxbody.partnerId
    request.prepayId = wxbody.prepayId
    request.packageValue = wxbody.packageValue
    request.nonceStr = wxbody.nonceStr
    request.timeStamp = wxbody.timeStamp
    request.sign = wxbody.sign
    createWx()?.sendReq(request)
}

data class Wxbody(
    val appId: String,
    val nonceStr: String,
    val packageValue: String,
    val partnerId: String,
    val prepayId: String,
    val sign: String,
    val timeStamp: String
)