package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.response.Banner
import retrofit2.http.GET

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/11/21
 * @description : TODO
</pre> *
 */
interface FastService {

    @GET("banner/json")
    suspend fun banner(): BaseResult<List<Banner>>
}