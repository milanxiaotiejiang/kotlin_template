package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.IViewModel
import com.seabreeze.robot.base.model.CoroutineState
import com.seabreeze.robot.base.ui.foundation.fragment.BaseFragment
import java.lang.reflect.ParameterizedType

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : Mvvm封装类
 * </pre>
 */
abstract class BaseVmFragment<out ViewModel : BaseViewModel, DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseFragment(), IViewModel<ViewModel> {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mDataBinding.lifecycleOwner = viewLifecycleOwner
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
        initViewModelActions()
    }

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

    abstract fun onInitDataBinding()

}