package com.thirtydays.kotlin.ui.article.list

import android.view.View
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.thirtydays.kotlin.R
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

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
            handlers = this@ArticleListActivity

            mDataBinding.includeList.viewModel = mViewModel
            mDataBinding.includeList.handlers = this@ArticleListActivity

            mDataBinding.includeListEmpty.viewModel = mViewModel
            mDataBinding.includeListEmpty.handlers = this@ArticleListActivity
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }

    }

    override fun initData() {

        mViewModel.loadArticleList(true)
    }

    override fun onEmptyClick(view: View) {

    }
}