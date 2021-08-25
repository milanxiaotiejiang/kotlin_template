package com.thirtydays.kotlin.ui.article.list

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.UIState
import com.seabreeze.robot.data.DataApplication.Companion.dataRepository
import com.seabreeze.robot.data.common.Common.Companion.PAGE_NO_START
import com.seabreeze.robot.data.common.Common.Companion.PAGE_SIZE
import com.seabreeze.robot.data.ext.SmartRefreshViewState
import com.seabreeze.robot.data.net.bean.response.Article
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.ArticleListAdapter

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/18
 * @description : TODO
 * </pre>
 */
class ArticleListViewModel : BaseViewModel() {

    val viewAdapter: ArticleListAdapter by lazy {
        ArticleListAdapter()
            .apply {
                addChildClickViewIds(R.id.tv_title)
                setOnItemChildClickListener { _, view, position ->
                    when (view.id) {
                        R.id.tv_title -> notifyItemChanged(
                            position,
                            ArticleListAdapter.CHANGE_COLOR
                        )
                    }
                }
            }
    }

    private val _data = MutableLiveData<List<Article>>()
    val state = Transformations.map(_data) {
        if (it.isEmpty())
            ArticleListViewState.Empty
        else
            ArticleListViewState.Listed
    }

    private var mPageNo = 0
    val viewState = MutableLiveData<SmartRefreshViewState>()

    val enableLoadMore = MutableLiveData<Boolean>().apply {
        postValue(true)
    }

    fun refresh() {
        loadArticleList(true)
    }

    fun loadMore() {
        loadArticleList(false)
    }

    fun loadArticleList(isRefresh: Boolean) {
        mPageNo = if (isRefresh) PAGE_NO_START else mPageNo + 1

        launch(
            uiState = UIState(
                isShowLoadingProgressBar = true,
                isShowLoadingView = true,
                isShowErrorToast = true
            ),
            block = {
                dataRepository.getArticleList(mPageNo)
            },
            success = {
                it.datas.run {
                    viewState.postValue(SmartRefreshViewState(isRefresh, true))
                    enableLoadMore.postValue(size >= PAGE_SIZE)
                    if (isRefresh) viewAdapter.setList(this)
                    else viewAdapter.addData(this)
                }
            },
            error = {
                viewState.postValue(
                    SmartRefreshViewState(isRefresh, isSuccess = false)
                )
            },
            complete = {
                _data.postValue(viewAdapter.data)
            }
        )

    }

    interface Handlers {
        fun onEmptyClick(view: View)
    }

    sealed class ArticleListViewState {
        object Empty : ArticleListViewState()
        object Listed : ArticleListViewState()

        fun isEmpty() = this is Empty
        fun isListed() = this is Listed
    }
}