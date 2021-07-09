package com.thirtydays.kotlin.ui.main.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.seabreeze.robot.base.router.pop
import com.seabreeze.robot.base.ui.fragment.BaseMvvmFragment
import com.seabreeze.robot.data.DataApplication.Companion.okHttpClient
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.HomeAdapter
import com.thirtydays.kotlin.mvvm.repository.HomeRepository
import com.thirtydays.kotlin.mvvm.vm.HomeViewModel
import com.thirtydays.kotlin.ui.ext.ExtActivity
import com.thirtydays.kotlin.ui.glide.GlideActivity
import com.thirtydays.kotlin.ui.hook.HookActivity
import com.thirtydays.kotlin.ui.hook.RoomActivity
import com.thirtydays.kotlin.ui.loadpage.CommodityActivity
import com.thirtydays.kotlin.ui.message.MessageListActivity
import com.thirtydays.kotlin.ui.simple.SimpleExampleActivity
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.Request
import java.io.IOException

class HomeFragment : BaseMvvmFragment<HomeRepository, HomeViewModel>() {

    override fun createViewModel() = ViewModelProvider(this)[HomeViewModel::class.java]

    override fun getLayoutId() = R.layout.fragment_home

    override fun requestData() {

        val adapter = HomeAdapter(
            mutableListOf(
                "retrofit 测试",
                "room 测试",
                "Java Hook",
                "Ext 测试",
                "刷新加载页面",
                "Glide",
                "极简单的页面请求"
            )
        )
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
//                    mViewModel.login("765151629@qq.com", "123456")
                    Thread {
                        try {
                            val request: Request = Request.Builder()
                                .url("https://github.com/")
                                .build()
                            val response = okHttpClient.newCall(request).execute()
                            val result = response.body!!.string()
//                            XLog.e(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }.start()
                }
                1 -> pop<RoomActivity>()
                2 -> pop<HookActivity>()
                3 -> pop<ExtActivity>()
                4 -> pop<CommodityActivity>()
                5 -> pop<GlideActivity>()
                6 -> pop<SimpleExampleActivity>()
            }
        }

        mViewModel.accountData.observe(this) {
            pop<MessageListActivity>()
        }
    }

}