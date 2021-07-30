package com.thirtydays.kotlin.ui.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.seabreeze.robot.base.ext.foundation.pop
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.thirtydays.kotlin.databinding.ActivityExtBinding
import com.thirtydays.kotlin.ui.ext.activity.TimeActivity
import com.thirtydays.kotlin.ui.ext.activity.ViewActivity

class ExtPresenter : BasePresenter<ExtActivity>()
class ExtActivity : BaseMvpActivity<ExtPresenter, ActivityExtBinding>() {

    override fun initData() {
        setSupportActionBar(mViewBinding.toolbar)
        mViewBinding.toolbarLayout.title = title
        mViewBinding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun onAppExt(view: View) {}
    fun onBrightnessExt(view: View) {}
    fun onCommonExt(view: View) {}
    fun onDensityExt(view: View) {}
    fun onEventUtils(view: View) {}
    fun onKeyboardExt(view: View) {}
    fun onMd5Ext(view: View) {}
    fun onMmkvExt(view: View) {}
    fun onNetworkExt(view: View) {}
    fun onNumberExt(view: View) {}
    fun onRegexExt(view: View) {}
    fun onResourceExt(view: View) {}
    fun onScreenExt(view: View) {}
    fun onSpanExt(view: View) {

    }

    fun onTimeExt(view: View) {
        pop<TimeActivity>()
    }

    fun onViewExt(view: View) {
        pop<ViewActivity>()
    }

    fun onViewPrefs(view: View) {

    }
}