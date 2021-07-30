package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seabreeze.robot.base.ui.foundation.fragment.BaseFragment

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : Mvvm封装类
 * </pre>
 */
abstract class SimpleFragment : BaseFragment() {

    protected open lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this::mRootView.isInitialized) {
            return mRootView
        }
        mRootView = inflater.inflate(getLayoutId(), container, false)
        return mRootView
    }

    protected abstract fun getLayoutId(): Int

}