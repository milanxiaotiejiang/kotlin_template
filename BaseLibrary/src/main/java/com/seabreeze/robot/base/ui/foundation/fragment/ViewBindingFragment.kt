package com.seabreeze.robot.base.ui.foundation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * </pre>
 */
abstract class ViewBindingFragment<VB : ViewBinding> : BaseFragment() {

    protected open lateinit var mViewBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            var bindClass: Class<*>? = null
            val actualTypeArguments = type.actualTypeArguments
            actualTypeArguments.forEach {
                val clazz = it as Class<*>
                if (clazz.name.endsWith("Binding"))
                    bindClass = clazz
            }

            bindClass?.let {
                val method = it.getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                mViewBinding = method.invoke(null, layoutInflater, container, false) as VB
            }
        }
        if (this::mViewBinding.isInitialized) {
            return mViewBinding.root
        } else {
            throw RuntimeException("type xxx Binding")
        }

    }

}