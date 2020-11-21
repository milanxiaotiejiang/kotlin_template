package com.seabreeze.robot.base.error

/**
 * User: milan
 * Time: 2020/4/20 19:25
 * Des:
 */
class BaseThrowable(
    val errorCode: String,
    val errorMessage: String
) : Throwable(errorMessage)