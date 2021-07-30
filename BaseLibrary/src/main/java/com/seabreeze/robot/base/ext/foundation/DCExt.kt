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
inline fun <reified T> BaseResult<T>.dcEither() =
    resultStatus.yes {
        Either.left(resultData)
    }.otherwise {
        Either.right(BaseThrowable.InsideThrowable(errorCode, errorMessage))
    }