package com.seabreeze.robot.data.net.service

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/27
 * @description : 图片接口
 * </pre>
 */
interface ImageService {
    /**
     * 下载图片
     */
    @Streaming
    @GET
    fun downloadPicFromNet(@Url fileUrl: String): Flowable<ResponseBody>
}