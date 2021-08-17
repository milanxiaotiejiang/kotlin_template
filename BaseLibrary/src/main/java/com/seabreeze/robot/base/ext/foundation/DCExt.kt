package com.seabreeze.robot.base.ext.foundation

import com.seabreeze.robot.base.model.BaseResult

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 数据转换工具类
 * </pre>
 */
//公司基类判断，下面为 wanandroid API
//inline fun <reified T> BaseResult<T>.dcEither() =
//    resultStatus.yes {
//        Either.left(resultData)
//    }.otherwise {
//        Either.right(BaseThrowable.InsideThrowable(errorCode, errorMsg))
//    }
inline fun <reified T> BaseResult<T>.dcEither() =
    (errorCode == 0).yes {
        Either.left(data)
    }.otherwise {
        Either.right(BaseThrowable.InsideThrowable(errorCode.toString(), errorMsg))
    }