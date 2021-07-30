package com.seabreeze.robot.base.framework.mvp

import com.seabreeze.robot.base.framework.mvp.view.IView
import com.trello.rxlifecycle3.LifecycleProvider

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des:
 */
interface IPresenter<out View : IView<IPresenter<View>>> {
    val mView: View
}

abstract class BasePresenter<out View : IView<BasePresenter<View>>> : IPresenter<View> {
    override lateinit var mView: @UnsafeVariance View

    lateinit var lifecycleProvider: LifecycleProvider<*>
}