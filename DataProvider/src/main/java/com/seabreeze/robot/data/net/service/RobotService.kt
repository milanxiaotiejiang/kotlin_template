package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.request.GithubLoginRequest
import com.seabreeze.robot.data.net.bean.response.*
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    suspend fun authorizations(@Body loginRequestData: GithubLoginRequest): UserAccessTokenData

    @GET("user")
    suspend fun fetchUserInfo(): UserInfoData

    @POST("/app/v1/login/email")
    suspend fun login(@Body body: RequestBody): BaseResult<AccountPO>

    @GET("/app/v1/message")
    fun message(@Query("startTime") startTime: String): Flowable<BaseResult<List<Message>>>

    @GET("/app/v1/commodity")
    suspend fun commodity(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): BaseResult<Pager<CommodityPO>>


    @GET("/app/v1/message/{messageId}")
    fun messageId(@Path("messageId") messageId: String): Flowable<BaseResult<Message>>

}