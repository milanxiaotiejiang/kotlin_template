package com.seabreeze.robot.data.net

import com.seabreeze.robot.data.DataApplication.Companion.retrofitFactory
import com.seabreeze.robot.data.net.api.ImageAPI
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.api.impl.ImageImpl
import com.seabreeze.robot.data.net.api.impl.RobotImpl
import com.seabreeze.robot.data.net.bean.request.UserLoginRequest
import com.seabreeze.robot.data.net.service.FastService

/**
 * User: milan
 * Time: 2020/4/9 17:29
 * Des:
 */
class DataRepository private constructor() : RobotAPI, ImageAPI {

    init {
        sRobotImplement = RobotImpl()
        sImageImplement = ImageImpl()
    }

    companion object {

        private lateinit var sRobotImplement: RobotImpl
        private lateinit var sImageImplement: ImageImpl

        // TODO:  DataRepository 此处开放更具简单的调用，不满足接口隔离原则，暂时支持 com.seabreeze.robot.data.net.service.SimpleService
        val sFastImplement: FastService = retrofitFactory.create(FastService::class.java)

        val INSTANCE: DataRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }
    }


    override fun downloadPicFromNet(fileUrl: String) = sImageImplement.downloadPicFromNet(fileUrl)

    override suspend fun userLogin(userLoginRequest: UserLoginRequest) =
        sRobotImplement.userLogin(userLoginRequest)

    override suspend fun userLogin(username: String, password: String) =
        sRobotImplement.userLogin(username, password)

    override suspend fun userLogin(map: Map<String, String>) = sRobotImplement.userLogin(map)

    override suspend fun userRegister(username: String, password: String, repassword: String) =
        sRobotImplement.userRegister(username, password, repassword)

    override suspend fun userLogout() = sRobotImplement.userLogout()

    override suspend fun getArticleList(pageNo: Int) = sRobotImplement.getArticleList(pageNo)

}