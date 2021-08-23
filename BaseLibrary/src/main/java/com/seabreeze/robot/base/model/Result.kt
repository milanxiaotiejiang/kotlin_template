package com.seabreeze.robot.base.model

/**
此为本公司接口返回，注意和 wanandroid 区分
data class BaseResult<T>(
val resultStatus: Boolean,
val errorCode: String,
val errorMessage: String,
val resultData: T
)
 */
data class BaseResult<T>(
    val errorCode: Int,//wanandroid 中判断 errorCode 为 0 正常
    val errorMsg: String,
    val `data`: T
)

data class ListModel<T>(
    val showData: List<T>? = null,
    val showError: String? = null,
    val showEnd: Boolean = false, // 加载更多
    val isRefresh: Boolean = false, // 刷新
    val isFinishRefresh: Boolean = true
)

data class Pager<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)