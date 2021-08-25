package com.seabreeze.robot.data.net.api.impl

import com.seabreeze.robot.data.common.Common.Companion.PAGE_SIZE
import com.seabreeze.robot.data.net.BaseImpl
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.bean.request.UserLoginRequest
import com.seabreeze.robot.data.net.service.RobotService


/**
 * User: milan
 * Time: 2020/4/9 17:32
 * Des:
 */
class RobotImpl : BaseImpl<RobotService>(), RobotAPI {

    override suspend fun userLogin(userLoginRequest: UserLoginRequest) =
        mService.userLogin(userLoginRequest)

    override suspend fun userLogin(username: String, password: String) =
        mService.userLogin(username, password)

    override suspend fun userLogin(map: Map<String, String>) =
        mService.userLogin(map)

    override suspend fun userRegister(username: String, password: String, repassword: String) =
        mService.userRegister(username, password, repassword)

    override suspend fun userLogout() = mService.userLogout()

    override suspend fun getArticleList(pageNo: Int) =  mService.articleList(pageNo, PAGE_SIZE)

}