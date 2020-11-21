package com.seabreeze.robot.data.net

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.DataApplication.Companion.retrofitFactory
import com.seabreeze.robot.data.net.api.ImageAPI
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.api.impl.ImageImpl
import com.seabreeze.robot.data.net.api.impl.RobotImpl
import com.seabreeze.robot.data.net.bean.response.Message
import com.seabreeze.robot.data.net.service.SimpleService
import io.reactivex.Flowable

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
        val sSimpleImplement: SimpleService = retrofitFactory.create(SimpleService::class.java)

        val INSTANCE: DataRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }
    }

    override suspend fun login(email: String, password: String) =
        sRobotImplement.login(email, password)


    override fun message() = sRobotImplement.message()

    override suspend fun commodity(pageNo: Int) = sRobotImplement.commodity(pageNo)

    override fun messageId(messageId: String): Flowable<BaseResult<Message>> =
        sRobotImplement.messageId(messageId)

    override fun downloadPicFromNet(fileUrl: String) = sImageImplement.downloadPicFromNet(fileUrl)

}