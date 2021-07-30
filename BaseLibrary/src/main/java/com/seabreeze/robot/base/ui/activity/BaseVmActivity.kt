package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.IViewModel
import com.seabreeze.robot.base.model.CoroutineState
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import java.lang.reflect.ParameterizedType


/**
 * User: milan
 * Time: 2020/3/24 16:35
 * Des: Mvvm 必须使用协程
 */
abstract class BaseVmActivity<out ViewModel : BaseViewModel, DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseActivity(), IViewModel<ViewModel> {

    lateinit var mDataBinding: DataBinding

    final override val mViewModel: ViewModel

    init {
        mViewModel = findViewModelClass().newInstance()
    }

    private fun findViewModelClass(): Class<ViewModel> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<ViewModel>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mDataBinding.lifecycleOwner = this

        onInitDataBinding()

        initViewModelActions()
        initData()
    }

    abstract fun onInitDataBinding()

    private fun initViewModelActions() {
        observe(mViewModel.error) {
            it.onError()
        }
        observe(mViewModel.statusLiveData) {
            when (it) {
                is CoroutineState.Loading -> {
                    showProgressDialog()
                }
                is CoroutineState.Finish -> {
                    dismissProgressDialog()
                }
                is CoroutineState.Error -> {
                    dismissProgressDialog()
                }
            }
        }
    }

    protected abstract fun initData()

}