package com.seabreeze.robot.data.net.api

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.request.UserLoginRequest
import com.seabreeze.robot.data.net.bean.response.UserInfo

/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotAPI {

    suspend fun userLogin(userLoginRequest: UserLoginRequest): BaseResult<UserInfo>

    suspend fun userLogin(username: String, password: String): BaseResult<UserInfo>

    suspend fun userLogin(map: Map<String, String>): BaseResult<UserInfo>

    suspend fun userRegister(
        username: String,
        password: String,
        repassword: String
    ): BaseResult<UserInfo>

    suspend fun userLogout(): BaseResult<Any>

}