package com.seabreeze.robot.data.net.api.impl

import com.seabreeze.robot.data.net.BaseImpl
import com.seabreeze.robot.data.net.api.ImageAPI
import com.seabreeze.robot.data.net.service.ImageService

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/27
 * @description : 图片请求
 * </pre>
 */
class ImageImpl : BaseImpl<ImageService>(), ImageAPI {
    override fun downloadPicFromNet(fileUrl: String) = mService.downloadPicFromNet(fileUrl)
}