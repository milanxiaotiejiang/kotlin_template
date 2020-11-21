package com.thirtydays.kotlin.ui.simple

import android.os.Looper
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.encryption
import com.seabreeze.robot.base.ext.gToJson
import com.seabreeze.robot.base.ext.launchRequest
import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.base.ui.activity.SimpleActivity
import com.seabreeze.robot.data.net.DataRepository.Companion.sSimpleImplement
import com.seabreeze.robot.data.net.bean.request.EmailLoginDTO
import com.seabreeze.robot.data.net.bean.response.AccountPO
import com.thirtydays.kotlin.R
import kotlinx.android.synthetic.main.activity_simple_example.*
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/11/21
 * @description : TODO
 * </pre>
 */
class SimpleExampleActivity : SimpleActivity() {
    override fun getLayoutId() = R.layout.activity_simple_example

    override fun initData() {
        tvSimple.setOnClickListener {
            clickSimple()
        }
    }

    private fun clickSimple() {
        tvContent.text = "请求前"
        XLog.e("Looper 1 ${Looper.getMainLooper() == Looper.myLooper()}")//true
        launchRequest(
            {
                XLog.e("Looper 2 ${Looper.getMainLooper() == Looper.myLooper()}")//无 io 为 true / 有 io 为 false
                requestApi()
            }, {
                XLog.e("Looper 3 ${Looper.getMainLooper() == Looper.myLooper()}")//true
                updateUi(it)
                XLog.e("Looper 4 ${Looper.getMainLooper() == Looper.myLooper()}")//true
            }, flowControl = true
        )
        XLog.e("Looper 5 ${Looper.getMainLooper() == Looper.myLooper()}")//true
    }

//    private suspend fun requestApiIo(): BaseResult<AccountPO> {
//        val withContext = withContext(Dispatchers.IO) {
//            XLog.e("Looper 21 ${Looper.getMainLooper() == Looper.myLooper()}")//false
//            return@withContext sSimpleImplement.login(
//                EmailLoginDTO(
//                    "765151629@qq.com",
//                    "123456".encryption()
//                ).gToJson().toRequestBody()
//            )
//        }
//        XLog.e("Looper 22 ${Looper.getMainLooper() == Looper.myLooper()}")//true
//        return withContext
//    }

    private suspend fun requestApi(): BaseResult<AccountPO> {
        return sSimpleImplement.login(
            EmailLoginDTO(
                "765151629@qq.com",
                "123456".encryption()
            ).gToJson().toRequestBody()
        )
    }

    private fun updateUi(it: AccountPO) {
        tvContent.text = it.toString()
    }
}