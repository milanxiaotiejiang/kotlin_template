package com.seabreeze.robot.third.ext

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Either
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.DataApplication.Companion.okHttpClient
import com.seabreeze.robot.third.ext.WxPay.createWx
import com.tencent.mm.opensdk.modelmsg.SendAuth
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Request


/**
 * User: milan
 * Time: 2020/9/14 21:09
 * Des:
 */
private const val SCOPE = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact"
fun AppCompatActivity.loginWx() {
    val req = SendAuth.Req()
    req.scope = SCOPE
    req.state = "none"
    createWx()?.sendReq(req)
}

@SuppressLint("CheckResult")
fun AppCompatActivity.loginWxRequest(
    code: String,
    result: (Either<WxResult, BaseThrowable>) -> Unit
) {
    Flowable.just(code)
        .map {
            return@map String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?"
                        + "appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                WX_APP_ID, WX_APP_SECRET, it
            )
        }
        .map {
            val request = Request.Builder()
                .url(it)
                .get()
                .build()
            val response = okHttpClient.newCall(request).execute()
            val body = response.body
            body?.let {
                val bytes = body.bytes()
                val httpResult = bytes.toString(charset("iso-8859-1"))
                val wxResult = httpResult.gToBean<WxResult>()
                wxResult?.let {
                    return@map Either.left(wxResult)
                }?.let {
                    Either.right(WxThrowable("wxResult is null"))
                }
            }?.let {
                Either.right(WxThrowable("oauth2 body is null"))
            }
        }
        .map { either ->
            either.fold({ wxResult ->
                val url = String.format(
                    "https://api.weixin.qq.com/sns/userinfo?" + "access_token=%s&openid=%s",
                    wxResult.access_token, wxResult.openid
                )
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                val response = okHttpClient.newCall(request).execute()
                val body = response.body
                if (body != null) {
                    val bytes = body.bytes()
                    val httpResult = bytes.toString(Charsets.UTF_8)
                    val wxInfo = httpResult.gToBean<WxInfo>()
                    wxResult.wxInfo = wxInfo
                }
                Either.left(wxResult)
            }, {
                Either.right(BaseThrowable.ExternalThrowable(it))
            })
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            it?.apply {
                result(this)
            }
        }, {
            result(Either.right(BaseThrowable.ExternalThrowable(it)))
        })

}

data class WxResult(
    val access_token: String,
    val expires_in: Long,
    val refresh_token: String,
    val openid: String,
    val scope: String,
    val unionid: String,
    val errcode: Int,
    val errmsg: String,
    var wxInfo: WxInfo?
)

data class WxInfo(
    val city: String,
    val country: String,
    val headimgurl: String,
    val language: String,
    val nickname: String,
    val openid: String,
    val privilege: List<Any>,
    val province: String,
    val sex: Int,
    val unionid: String
)

class WxThrowable(error: String) : Throwable(error)