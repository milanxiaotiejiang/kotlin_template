package com.seabreeze.robot.base.ui.foundation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * @description : ViewBinding反射初始化、其他基类初始化
 * </pre>
 */
abstract class ViewBindingActivity<VB : ViewBinding> : BaseActivity() {

    protected open lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val method = it.getMethod("inflate", LayoutInflater::class.java)
                mViewBinding = method.invoke(null, layoutInflater) as VB
            }

            if (this::mViewBinding.isInitialized) {
                setContentView(mViewBinding.root)
            } else {
                throw RuntimeException("type xxx Binding")
            }
        }
    }
}