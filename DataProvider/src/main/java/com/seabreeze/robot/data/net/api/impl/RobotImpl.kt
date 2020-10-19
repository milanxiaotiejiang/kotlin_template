package com.seabreeze.robot.data.net.api.impl

import com.seabreeze.robot.base.ext.FORMAT_YMDHMS
import com.seabreeze.robot.base.ext.encryption
import com.seabreeze.robot.base.ext.gToJson
import com.seabreeze.robot.base.ext.getTimeFormatStr
import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.common.Common.Companion.REQUEST_PAGE_SIZE
import com.seabreeze.robot.data.net.BaseImpl
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.bean.request.EmailLoginDTO
import com.seabreeze.robot.data.net.bean.response.Message
import com.seabreeze.robot.data.net.service.RobotService
import io.reactivex.Flowable
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*


/**
 * User: milan
 * Time: 2020/4/9 17:32
 * Des:
 */
class RobotImpl : BaseImpl<RobotService>(), RobotAPI {

    override suspend fun login(email: String, password: String) =
        mService.login(
            EmailLoginDTO(
                email,
                password.encryption()
            ).gToJson().toRequestBody()
        )

    override fun message(): Flowable<BaseResult<List<Message>>> {
        val c = Calendar.getInstance()
        c.time = Date()
        c.add(Calendar.MONTH, -1)
        val m = c.time

        val timeFormatLong = getTimeFormatStr(FORMAT_YMDHMS, m)
        return mService.message(timeFormatLong)
    }

    override suspend fun commodity(pageNo: Int) = mService.commodity(pageNo, REQUEST_PAGE_SIZE)

    override fun messageId(messageId: String): Flowable<BaseResult<Message>> =
        mService.messageId(messageId)

}