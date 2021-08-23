package com.thirtydays.kotlin.ui.main.home

import androidx.recyclerview.widget.DividerItemDecoration
import com.graves.rubbishbag.startCommunicationThread
import com.seabreeze.robot.base.ext.foundation.pop
import com.seabreeze.robot.base.ui.fragment.BaseVmFragment
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.HomeAdapter
import com.thirtydays.kotlin.databinding.FragmentHomeBinding
import com.thirtydays.kotlin.mvvm.vm.HomeViewModel
import com.thirtydays.kotlin.ui.article.list.ArticleListActivity
import com.thirtydays.kotlin.ui.ext.ExtActivity
import com.thirtydays.kotlin.ui.glide.GlideActivity
import com.thirtydays.kotlin.ui.hook.HookActivity
import com.thirtydays.kotlin.ui.hook.RoomActivity
import com.thirtydays.kotlin.ui.simple.SimpleExampleActivity

class HomeFragment : BaseVmFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
//        mViewModel.accountData.observe(this) {
//            pop<MessageListActivity>()
//        }
    }

    override fun requestData() {

        val adapter = HomeAdapter(
            mutableListOf(
                "单线程示例",
                "room 测试",
                "Java Hook",
                "Ext 测试",
                "刷新加载页面",
                "Glide列表图库选择",
                "极简单的页面请求"
            )
        )
        mDataBinding.recyclerView.adapter = adapter
        mDataBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
                    startCommunicationThread()
                }
                1 -> pop<RoomActivity>()
                2 -> pop<HookActivity>()
                3 -> pop<ExtActivity>()
                4 -> pop<ArticleListActivity>()
                5 -> pop<GlideActivity>()
                6 -> pop<SimpleExampleActivity>()
            }
        }


    }

}