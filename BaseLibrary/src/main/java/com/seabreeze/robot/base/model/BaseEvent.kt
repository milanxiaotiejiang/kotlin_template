package com.seabreeze.robot.base.model

import retrofit2.HttpException

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/3
 * desc   :
 * </pre>
 */
open class BaseEvent<T>(var ok: Boolean, var bean: T) {
    private var code: Int = 0

    constructor(code: Int, bean: T) : this(true, bean) {
        this.code = code
    }

    fun code() = code
}


class TokenInvalidEvent(error: HttpException) : BaseEvent<HttpException>(true, error)