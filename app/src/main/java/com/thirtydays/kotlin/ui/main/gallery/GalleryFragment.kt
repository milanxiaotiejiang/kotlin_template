package com.thirtydays.kotlin.ui.main.gallery

import androidx.recyclerview.widget.DividerItemDecoration
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.router.startLogin
import com.seabreeze.robot.base.router.startPay
import com.seabreeze.robot.base.router.startShare
import com.seabreeze.robot.base.ui.fragment.BaseMvpFragment
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class GalleryPresenter : BasePresenter<GalleryFragment>()

class GalleryFragment : BaseMvpFragment<GalleryPresenter>() {
    override fun requestData() {
        val adapter = HomeAdapter(
            mutableListOf("第三方登录", "分享", "支付")
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
                0 -> startLogin()
                1 -> startShare()
                2 -> startPay()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_gallery

}