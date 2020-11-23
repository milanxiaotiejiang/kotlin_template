package com.seabreeze.robot.base.model

data class BaseResult<T>(
    val resultStatus: Boolean,
    val errorCode: String,
    val errorMessage: String,
    val resultData: T
)

data class BaseNullResult<T>(
    val resultStatus: Boolean,
    val errorCode: String?,
    val errorMessage: String?,
    val resultData: T?
)

data class ListModel<T>(
    val showData: List<T>? = null,
    val showError: String? = null,
    val showEnd: Boolean = false, // 加载更多
    val isRefresh: Boolean = false, // 刷新
    val isFinishRefresh: Boolean = true
)

data class Pager<T>(
    val current: Int,
    val pages: Int,
    val records: List<T>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int,
    var pageTime: Long
)