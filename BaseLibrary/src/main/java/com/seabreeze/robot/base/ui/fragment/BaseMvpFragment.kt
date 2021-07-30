package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.ldoublem.loadingviewlib.view.LVCircularSmile
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.view.inflate
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.framework.mvp.view.BaseView
import com.seabreeze.robot.base.ui.foundation.fragment.RxFragment
import java.lang.reflect.ParameterizedType

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des:
 */
abstract class BaseMvpFragment<out Presenter : BasePresenter<BaseView<Presenter>>, VB : ViewBinding> :
    RxFragment<VB>(), BaseView<Presenter> {

    final override val mPresenter: Presenter

    init {
        mPresenter = findPresenterClass().newInstance()
        mPresenter.mView = this
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<Presenter>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    private lateinit var mLoadingDialog: MaterialDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.lifecycleProvider = this

        initCustomDialog()

    }

    private lateinit var loadView: View
    private lateinit var lvCircularSmile: LVCircularSmile
    private lateinit var loadTip: TextView

    private fun initCustomDialog() {
        //初始加载框
        loadView = inflate(R.layout.loading, null)
        lvCircularSmile = loadView.find(R.id.lVCircularSmile)
        loadTip = loadView.find(R.id.loadTip)
        mLoadingDialog = MaterialDialog(requireContext(), ModalDialog)
            .customView(view = loadView, dialogWrapContent = true)
    }

    override fun onDestroyView() {
        hideLoading()
        super.onDestroyView()
    }

    override fun showToast(msg: String) {
        super.showToast(msg)
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        if (!mLoadingDialog.isShowing) {
            mLoadingDialog.show {
                title(text = title)
                lifecycleOwner(this@BaseMvpFragment)
                loadTip.text = tip
                lvCircularSmile.setViewColor(color)
                lvCircularSmile.startAnim()
            }
        }
    }

    override fun hideLoading() {
        lvCircularSmile.stopAnim()
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

    override fun onError(throwable: BaseThrowable) {
        super.onError(throwable)
    }

}
