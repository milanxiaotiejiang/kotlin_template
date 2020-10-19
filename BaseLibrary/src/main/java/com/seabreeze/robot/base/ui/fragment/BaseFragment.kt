package com.seabreeze.robot.base.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.seabreeze.robot.base.ui.ProgressDialogFragment
import com.seabreeze.robot.base.ui.rx.RxFragment

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des:
 */
open class BaseFragment : RxFragment()

abstract class ImmersionFragment : RxFragment(), SimpleImmersionOwner {
    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return true
    }

    open fun setStatusBar(view: View) {
        val statusHeight = getStatusBarHeight()
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            statusHeight
        )
    }

    private fun getStatusBarHeight(): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            return resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}

/**
 * BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
 * 会让fragment的生命周期默认停在onStart，搭配viewpager使用，只有当前被选中的page会调用onResume
 */
abstract class LazyLoadFragment : ImmersionFragment() {
    //是否懒加载
    open var isLazyLoad = true

    //是否加载数据（暂时用于第一次加载判断，以后也许会有其他情况）
    private var isNeedLoad = true

    private var mRootView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLazyLoad) {
            requestData()
        }
    }

    override fun onResume() {
        super.onResume()
        //如果是第一次且是懒加载
        //执行初始化方法
        if (isNeedLoad && isLazyLoad) {
            requestData()
            //数据已加载，置false，避免每次切换都重新加载数据
            isNeedLoad = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView != null) {
            return mRootView
        }
        mRootView = initRootView(inflater, container, savedInstanceState)
        return mRootView
    }

    open fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(getLayoutId(), container, false)
        return mRootView
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun requestData()

    private lateinit var progressDialogFragment: ProgressDialogFragment

    /**
     * 显示加载(转圈)对话框
     */
    fun showProgressDialog(message: String) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(childFragmentManager, message, false)
        }
    }

    /**
     * 隐藏加载(转圈)对话框
     */
    fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }
}
