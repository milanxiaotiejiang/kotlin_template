package com.seabreeze.robot.data.ext

import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/23
 * @description : TODO
 * </pre>
 */

data class SmartRefreshViewState(
    val isRefresh: Boolean = false, val isSuccess: Boolean = false
)

@BindingAdapter(
    value = ["viewState"],
    requireAll = false
)
fun SmartRefreshLayout.viewState(smartRefreshViewState: SmartRefreshViewState?) {
    smartRefreshViewState?.run {
        if (isRefresh)
            finishRefresh(isSuccess)
        else
            finishLoadMore(isSuccess)
    }
}

@BindingAdapter(
    value = ["enableLoadMore"],
    requireAll = false
)
fun SmartRefreshLayout.enableLoadMore(enableLoadMore: Boolean = true) {
    setEnableLoadMore(enableLoadMore)
}

@BindingAdapter(
    value = ["onRefreshListener"],
    requireAll = false
)
fun SmartRefreshLayout.onRefreshListener(
    refreshListener: OnRefreshListener?
) {
    setOnRefreshListener(refreshListener)
}

@BindingAdapter(
    value = ["onLoadMoreListener"],
    requireAll = false
)
fun SmartRefreshLayout.onLoadMoreListener(
    loadMoreListener: OnLoadMoreListener
) {
    setOnLoadMoreListener(loadMoreListener)
}