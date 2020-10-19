package com.seabreeze.robot.data.net.api

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.response.AccountPO
import com.seabreeze.robot.data.net.bean.response.CommodityPO
import com.seabreeze.robot.data.net.bean.response.Message
import com.seabreeze.robot.data.net.bean.response.Pager
import io.reactivex.Flowable

/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotAPI {

    suspend fun login(email: String, password: String): BaseResult<AccountPO>

    fun message(): Flowable<BaseResult<List<Message>>>

    suspend fun commodity(pageNo: Int): BaseResult<Pager<CommodityPO>>

    fun messageId(messageId: String): Flowable<BaseResult<Message>>

}