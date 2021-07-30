package com.seabreeze.robot.base.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/19
 * @description : 配合懒加载实现
 * </pre>
 */
open class RxPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: MutableList<String>?
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    constructor(fm: FragmentManager, fragments: List<Fragment>) : this(fm, fragments, null)

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.let {
            titles[position]
        }?.let {
            super.getPageTitle(position)
        }
    }
}

open class RxStatePagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: MutableList<String>?
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    constructor(fm: FragmentManager, fragments: List<Fragment>) : this(fm, fragments, null)

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.let {
            titles[position]
        }?.let {
            super.getPageTitle(position)
        }
    }
}