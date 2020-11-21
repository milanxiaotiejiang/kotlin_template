package com.seabreeze.robot.base.ext

import com.seabreeze.robot.base.error.BaseThrowable
import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.base.model.Either

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : 数据转换工具类
 * </pre>
 */
inline fun <reified T> BaseResult<T>.dcEither() =
    resultStatus.yes {
        Either.left(resultData)
    }.otherwise {
        Either.right(BaseThrowable(errorCode, errorMessage))
    }