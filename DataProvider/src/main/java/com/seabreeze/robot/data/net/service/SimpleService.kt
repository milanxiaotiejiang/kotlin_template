package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.response.AccountPO
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/11/21
 * @description : TODO
</pre> *
 */
interface SimpleService {
    @POST("/app/v1/login/email")
    suspend fun login(@Body body: RequestBody): BaseResult<AccountPO>
}