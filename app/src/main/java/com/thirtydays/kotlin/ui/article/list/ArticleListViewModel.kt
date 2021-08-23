package com.thirtydays.kotlin.ui.article.list

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.UIState
import com.seabreeze.robot.data.DataApplication
import com.seabreeze.robot.data.net.bean.response.Article

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

    val data = MutableLiveData<List<Article>>()

    val state = Transformations.map(data) {
        if (it.isEmpty())
            ArticleListViewState.Empty
        else
            ArticleListViewState.Listed
    }

    fun loadArticleList() {
        launch(
            uiState = UIState(
                isShowLoadingProgressBar = true,
                isShowLoadingView = true,
                isShowErrorView = true,
                isShowErrorToast = true
            ),
            block = {
                DataApplication.dataRepository.getArticleList(0)
            },
            success = {
                data.postValue(it.datas)
            }
        )

    }

    fun removeFavoriteCharacter(article: Article) {

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