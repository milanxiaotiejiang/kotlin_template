package com.seabreeze.robot.data.net.api

import io.reactivex.Flowable
import okhttp3.ResponseBody

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/27
 * @description : 图片接口
 * </pre>
 */
interface ImageAPI {
    fun downloadPicFromNet(fileUrl: String): Flowable<ResponseBody>
}