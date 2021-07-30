package com.seabreeze.robot.base.ui.foundation.fragment

import android.os.Bundle
import android.view.View

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * @description : BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
 * 会让fragment的生命周期默认停在onStart，搭配viewpager使用，只有当前被选中的page会调用onResume
 * </pre>
 */
abstract class LazyLoadFragment : ImmersionFragment() {
    //是否懒加载
    protected open var isLazyLoad = true

    //是否加载数据（暂时用于第一次加载判断，以后也许会有其他情况）
    private var isNeedLoad = true

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

    protected abstract fun requestData()

}