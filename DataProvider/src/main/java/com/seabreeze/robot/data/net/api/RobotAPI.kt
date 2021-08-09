package com.seabreeze.robot.data.net.api

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.request.GithubLoginRequest
import com.seabreeze.robot.data.net.bean.response.*
import io.reactivex.Flowable
import retrofit2.http.Body

/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotAPI {

    suspend fun authorizations(@Body loginRequestData: GithubLoginRequest): UserAccessTokenData

    suspend fun fetchUserInfo(): UserInfoData

    suspend fun login(email: String, password: String): BaseResult<AccountPO>

    fun message(): Flowable<BaseResult<List<Message>>>

    suspend fun commodity(pageNo: Int): BaseResult<Pager<CommodityPO>>

    fun messageId(messageId: String): Flowable<BaseResult<Message>>

}