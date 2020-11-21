package com.seabreeze.robot.base.ui.fragment

import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.Block
import com.seabreeze.robot.base.ext.postEvent
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.model.TokenInvalidEvent
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.vm.ModelView
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : Mvvm封装类
 * </pre>
 */
abstract class SimpleFragment : LazyLoadFragment(), SimpleImmersionOwner, ModelView {

    override fun onDestroyView() {
        hideLoading()
        super.onDestroyView()
    }

    fun launch(block: Block) {
        lifecycleScope.launch {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e)
            }
        }
    }

    override fun showToast(msg: String) {
        XLog.e(msg)
//        Alerter.create(activity)
//            .setText(msg)
//            .show()
        activity?.runOnUiThread { toast { msg } }
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        showProgressDialog(tip)
    }

    override fun hideLoading() {
        dismissProgressDialog()
    }

    override fun onError(throwable: Throwable) {
        XLog.e(throwable.message)
        hideLoading()
        throwable.message?.let { showToast(it) }
        if (throwable is HttpException) {
            when (throwable.code()) {
                401 -> {
                    startMain(true)
                    postEvent(TokenInvalidEvent(throwable))
                }
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

}