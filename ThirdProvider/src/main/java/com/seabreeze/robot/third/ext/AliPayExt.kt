package com.seabreeze.robot.third.ext

import android.annotation.SuppressLint
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/14
 * @description : https://opendocs.alipay.com/open/204/105296
 * </pre>
 */
const val ALI_PAY_SUCCESSFUL = "9000"
const val ALI_PAY_BEING = "8000"
const val ALI_PAY_FAILED = "4000"
const val ALI_PAY_REPEAT_REQUESTS = "5000"
const val ALI_PAY_USER_CANCELLATION = "6001"
const val ALI_PAY_NETWORK_ERROR = "6002"
const val ALI_PAY_UNKNOWN = "6004"


data class PayResult(
    var resultStatus: String? = null,
    var result: String? = null,
    var memo: String? = null
)

@SuppressLint("CheckResult")
fun RxAppCompatActivity.payAli(
    orderInfo: String,
    result: (result: PayResult) -> Unit
) =
    Flowable.just(orderInfo)
        .map {
            val payTask = PayTask(this)
            return@map payTask.payV2(orderInfo, true)
        }
        .map { rawResult ->
            val payResult = PayResult()
            for (key in rawResult.keys) {
                when {
                    TextUtils.equals(key, "resultStatus") -> {
                        payResult.resultStatus = rawResult[key]
                    }
                    TextUtils.equals(key, "result") -> {
                        payResult.result = rawResult[key]
                    }
                    TextUtils.equals(key, "memo") -> {
                        payResult.memo = rawResult[key]
                    }
                }
            }
            return@map payResult
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {

            toast {
                when (it.resultStatus) {
                    ALI_PAY_SUCCESSFUL -> "支付成功"
                    ALI_PAY_BEING -> "正在处理中"
                    ALI_PAY_FAILED -> "支付失败"
                    ALI_PAY_REPEAT_REQUESTS -> "重复请求"
                    ALI_PAY_USER_CANCELLATION -> "支付取消"
                    ALI_PAY_NETWORK_ERROR -> "网络连接出错"
                    ALI_PAY_UNKNOWN -> "支付结果未知"
                    else -> "其它支付错误"
                }
            }
            result(it)
        }
