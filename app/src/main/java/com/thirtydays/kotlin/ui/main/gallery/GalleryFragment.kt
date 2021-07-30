package com.thirtydays.kotlin.ui.main.gallery

import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.router.startLogin
import com.seabreeze.robot.base.router.startPay
import com.seabreeze.robot.base.router.startShare
import com.seabreeze.robot.base.ui.fragment.BaseMvpFragment
import com.thirtydays.kotlin.adapter.HomeAdapter
import com.thirtydays.kotlin.databinding.FragmentGalleryBinding

class GalleryPresenter : BasePresenter<GalleryFragment>()

class GalleryFragment : BaseMvpFragment<GalleryPresenter, FragmentGalleryBinding>() {

    override fun requestData() {
        val adapter = HomeAdapter(
            mutableListOf("第三方登录", "分享", "支付")
        )

//        mViewBinding.re.adapter = adapter
//        mViewBinding.recyclerView.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                DividerItemDecoration.VERTICAL
//            )
//        )
        adapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> startLogin()
                1 -> startShare()
                2 -> startPay()
            }
        }
    }

}