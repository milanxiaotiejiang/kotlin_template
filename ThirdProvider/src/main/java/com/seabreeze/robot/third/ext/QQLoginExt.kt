package com.seabreeze.robot.third.ext

import com.seabreeze.robot.base.ext.gToBean
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.seabreeze.robot.third.ext.QQLogin.createQQ
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

/**
 * User: milan
 * Time: 2020/9/14 22:22
 * Des:
 */
const val QQ_APP_ID: String = "101906416"
private const val APP_AUTHORITIES = ".fileprovider";

object QQLogin {
    private var qqAPI: Tencent? = null
    fun RxAppCompatActivity.createQQ(): Tencent {
        if (qqAPI != null) {
            return qqAPI!!
        }
        qqAPI = Tencent.createInstance(
            QQ_APP_ID, this.applicationContext,
            packageName + APP_AUTHORITIES
        )
        return qqAPI!!
    }
}

fun RxAppCompatActivity.loginQQ(listener: IUiListener) {
    createQQ().login(this, "all", listener)
}

fun RxAppCompatActivity.loginQQInfo(
    thirdQQ: ThirdQQ,
    complete: (thirdQQ: ThirdQQ) -> Unit,
    error: (error: UiError) -> Unit,
    cancel: () -> Unit = { toast { "取消QQ登录" } }
) {
    val qqToken = createQQ().qqToken
    createQQ().openId = thirdQQ.openid
    createQQ().setAccessToken(thirdQQ.access_token, thirdQQ.expires_in.toString())
    val userInfo = UserInfo(this, qqToken)
    userInfo.getUserInfo(object : IUiListener {
        override fun onComplete(response: Any?) {
            response?.let {
                if (it is JSONObject) {
                    val thirdQQInfo = it.toString().gToBean<ThirdQQInfo>()
                    thirdQQInfo?.let { qqInfo ->
                        thirdQQ.thirdQQInfo = qqInfo
                        complete(thirdQQ)
                    }
                }
            }

        }

        override fun onError(error: UiError?) {
            error?.let { error(it) }
        }

        override fun onCancel() {
            cancel()
        }
    })
}

data class ThirdQQ(
    val access_token: String,
    val authority_cost: Int,
    val code: String,
    val expires_in: Int,
    val expires_time: Long,
    val login_cost: Int,
    val msg: String,
    val openid: String,
    val pay_token: String,
    val pf: String,
    val pfkey: String,
    val proxy_code: String,
    val proxy_expires_in: Int,
    val query_authority_cost: Int,
    val ret: Int,
    var thirdQQInfo: ThirdQQInfo?
)

data class ThirdQQInfo(
    val city: String,
    val constellation: String,
    val figureurl: String,
    val figureurl_1: String,
    val figureurl_2: String,
    val figureurl_qq: String,
    val figureurl_qq_1: String,
    val figureurl_qq_2: String,
    val figureurl_type: String,
    val gender: String,
    val gender_type: Int,
    val is_lost: Int,
    val is_yellow_vip: String,
    val is_yellow_year_vip: String,
    val level: String,
    val msg: String,
    val nickname: String,
    val province: String,
    val ret: Int,
    val vip: String,
    val year: String,
    val yellow_vip_level: String
)