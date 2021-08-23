package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.base.model.Pager
import com.seabreeze.robot.data.net.bean.request.UserLoginRequest
import com.seabreeze.robot.data.net.bean.response.Article
import com.seabreeze.robot.data.net.bean.response.UserInfo
import retrofit2.http.*


/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des: https://www.wanandroid.com/ API
 */
interface RobotService {

    // wanandroid API 不支持 @Body 方式，此处仅当作一个示例，推荐使用
    @POST("user/login")
    suspend fun userLogin(@Body userLoginRequest: UserLoginRequest): BaseResult<UserInfo>

    //可以使用此种方式
    @FormUrlEncoded
    @POST("user/login")
    suspend fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResult<UserInfo>

    // 最不推荐的一种方式
    @FormUrlEncoded
    @POST("user/login")
    suspend fun userLogin(@FieldMap map: Map<String, String>): BaseResult<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun userRegister(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResult<UserInfo>

    @GET("user/logout/json")
    suspend fun userLogout(): BaseResult<Any>

    @GET("article/list/{page_no}/json")
    suspend fun articleList(@Path("page_no") pageNo: Int): BaseResult<Pager<Article>>

}