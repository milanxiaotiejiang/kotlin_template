package com.seabreeze.robot.base.model

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/3
 * desc   :
 * </pre>
 */
open class BaseEvent<T>(var bean: T? = null)

class TokenInvalidEvent() : BaseEvent<Any>()