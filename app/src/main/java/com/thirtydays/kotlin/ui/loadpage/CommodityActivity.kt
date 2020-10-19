package com.thirtydays.kotlin.ui.loadpage

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.elvishew.xlog.XLog
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ui.activity.BaseMvvmActivity
import com.seabreeze.robot.base.widget.loadpage.LoadPageStatus
import com.seabreeze.robot.base.widget.loadpage.LoadPageViewForStatus
import com.seabreeze.robot.base.widget.loadpage.SimplePageViewForStatus
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.DeviceAdapter
import com.thirtydays.kotlin.mvvm.repository.CommodityRepository
import com.thirtydays.kotlin.mvvm.vm.CommodityViewModel
import kotlinx.android.synthetic.main.activity_commodity.*
import kotlinx.android.synthetic.main.activity_message_list.commonTitleBar
import kotlinx.android.synthetic.main.activity_message_list.recyclerView
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class CommodityActivity : BaseMvvmActivity<CommodityRepository, CommodityViewModel>(),
    OnLoadMoreListener {

    override fun createViewModel() = ViewModelProvider(this)[CommodityViewModel::class.java]

    private val loadPageView = SimplePageViewForStatus()
    private var rootView: LoadPageViewForStatus? = null

    private var mAdapter = DeviceAdapter()

    override fun getLayoutId() = R.layout.activity_commodity

    override fun initViewModel() {
        mViewModel.run {
            loadPageLiveData.observe(
                this@CommodityActivity, Observer<LoadPageStatus> { loadPageStatus ->
                    rootView?.let { rootView ->
                        loadPageView.convert(
                            rootView,
                            loadPageStatus = loadPageStatus
                        )
                        mAdapter.setEmptyView(rootView)
                    }
                })
            listModelLiveData.observe(this@CommodityActivity, Observer {
                it.showData?.let { list ->
                    mAdapter.run {
                        if (it.isRefresh)
                            setList(list)
                        else
                            addData(list)
                        loadMoreModule.isEnableLoadMore = true
                        loadMoreModule.loadMoreComplete()
                    }
                }
                it.showError?.let { errorMsg ->//加载失败处理
                    mAdapter.loadMoreModule.loadMoreFail()
                    showToast(errorMsg)
                }
                if (it.isRefresh)
                    refreshLayout.finishRefresh(it.isFinishRefresh)
                if (it.showEnd)
                    mAdapter.loadMoreModule.loadMoreEnd()
            })
        }
    }

    override fun initData() {

        addDisposable(commonTitleBar.leftImageButton.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })

        rootView = loadPageView.getRootView(this).apply {
            failView.setOnClickListener { refresh() }
            noNetView.setOnClickListener { refresh() }
            emptyView.setOnClickListener { showToast("emptyView") }
        }

        recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    this@CommodityActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mAdapter
        }

        mAdapter.apply {
            loadMoreModule.setOnLoadMoreListener(this@CommodityActivity)
            setOnItemClickListener { _, _, position ->
                XLog.e("$position")
            }
        }

        refreshLayout.setOnRefreshListener { refresh() }
        refreshLayout.setEnableLoadMore(false)

        refresh()
    }

    private fun refresh() {
        mViewModel.commodity(true, mAdapter.data.size)
    }

    override fun onLoadMore() {
        mViewModel.commodity(false, mAdapter.data.size)
    }

}
