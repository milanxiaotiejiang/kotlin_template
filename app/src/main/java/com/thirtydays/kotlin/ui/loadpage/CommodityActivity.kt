//package com.thirtydays.kotlin.ui.loadpage
//
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.DividerItemDecoration
//import com.chad.library.adapter.base.listener.OnLoadMoreListener
//import com.seabreeze.robot.base.ext.view.setOnIntervalClickListener
//import com.seabreeze.robot.base.ui.activity.BaseVmActivity
//import com.seabreeze.robot.base.widget.loadpage.LoadPageStatus
//import com.seabreeze.robot.base.widget.loadpage.LoadPageViewForStatus
//import com.seabreeze.robot.base.widget.loadpage.SimplePageViewForStatus
//import com.thirtydays.kotlin.R
//import com.thirtydays.kotlin.databinding.ActivityCommodityBinding
//import com.thirtydays.kotlin.mvvm.vm.CommodityViewModel
//
///**
// * <pre>
// * author : 76515
// * time   : 2020/6/30
// * desc   :
// * </pre>
// */
//class CommodityActivity :
//    BaseVmActivity<CommodityViewModel, ActivityCommodityBinding>(R.layout.activity_commodity),
//    OnLoadMoreListener {
//
//    private val loadPageView = SimplePageViewForStatus()
//    private var rootView: LoadPageViewForStatus? = null
//
////    private var mAdapter = DeviceAdapter()
//
//    override fun onInitDataBinding() {
//        mDataBinding.viewModel = mViewModel
//        mViewModel.run {
//            loadPageLiveData.observe(
//                this@CommodityActivity, Observer<LoadPageStatus> { loadPageStatus ->
//                    rootView?.let { rootView ->
//                        loadPageView.convert(
//                            rootView,
//                            loadPageStatus = loadPageStatus
//                        )
////                        mAdapter.setEmptyView(rootView)
//                    }
//                })
////            listModelLiveData.observe(this@CommodityActivity, Observer {
////                it.showData?.let { list ->
////                    mAdapter.run {
////                        if (it.isRefresh)
////                            setList(list)
////                        else
////                            addData(list)
////                        loadMoreModule.isEnableLoadMore = true
////                        loadMoreModule.loadMoreComplete()
////                    }
////                }
////                it.showError?.let { errorMsg ->//加载失败处理
////                    mAdapter.loadMoreModule.loadMoreFail()
////                    showToast(errorMsg)
////                }
////                if (it.isRefresh)
////                    mDataBinding.refreshLayout.finishRefresh(it.isFinishRefresh)
////                if (it.showEnd)
////                    mAdapter.loadMoreModule.loadMoreEnd()
////            })
//        }
//    }
//
//    override fun initData() {
//
//        mDataBinding.commonTitleBar.leftImageButton.setOnIntervalClickListener {
//            finish()
//        }
//
//        rootView = loadPageView.getRootView(this).apply {
//            failView.setOnClickListener { refresh() }
//            noNetView.setOnClickListener { refresh() }
//            emptyView.setOnClickListener { showToast("emptyView") }
//        }
//
//        mDataBinding.recyclerView.apply {
//            addItemDecoration(
//                DividerItemDecoration(
//                    this@CommodityActivity,
//                    DividerItemDecoration.VERTICAL
//                )
//            )
////            adapter = mAdapter
//        }
//
////        mAdapter.apply {
////            loadMoreModule.setOnLoadMoreListener(this@CommodityActivity)
////            setOnItemClickListener { _, _, position ->
////                XLog.e("$position")
////            }
////        }
//
//        mDataBinding.refreshLayout.setOnRefreshListener { refresh() }
//        mDataBinding.refreshLayout.setEnableLoadMore(false)
//
//        refresh()
//    }
//
//    private fun refresh() {
////        mViewModel.commodity(true, mAdapter.data.size)
//    }
//
//    override fun onLoadMore() {
////        mViewModel.commodity(false, mAdapter.data.size)
//    }
//
//}
