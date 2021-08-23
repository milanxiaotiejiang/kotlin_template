package com.thirtydays.kotlin.ui.article.list

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.ArticleListAdapter
import com.thirtydays.kotlin.adapter.ArticleListTouchHelper
import com.thirtydays.kotlin.databinding.ActivityArticleListBinding

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/18
 * @description : TODO
 * </pre>
 */
class ArticleListActivity :
    BaseVmActivity<ArticleListViewModel, ActivityArticleListBinding>(R.layout.activity_article_list),
    ArticleListViewModel.Handlers {

    lateinit var viewAdapter: ArticleListAdapter

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
            handlers = this@ArticleListActivity
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }

        observe(mViewModel.data) {
            val data = it
            viewAdapter.submitList(data)
        }

    }

    override fun initData() {

        viewAdapter = ArticleListAdapter()

        mDataBinding.includeList.articleRecyclerView.apply {
            adapter = viewAdapter

            ItemTouchHelper(
                ArticleListTouchHelper {
                    mViewModel.removeFavoriteCharacter(viewAdapter.currentList[it])
                }
            ).attachToRecyclerView(this)
        }

        mViewModel.loadArticleList()
    }

    override fun onEmptyClick(view: View) {


    }
}