package com.thirtydays.kotlin.ui.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.router.pop
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.ui.ext.activity.TimeActivity
import com.thirtydays.kotlin.ui.ext.activity.ViewActivity
import kotlinx.android.synthetic.main.activity_ext.*

class ExtPresenter : BasePresenter<ExtActivity>()
class ExtActivity : BaseMvpActivity<ExtPresenter>() {

    override fun getLayoutId() = R.layout.activity_ext

    override fun initData() {
        setSupportActionBar(toolbar)
        toolbar_layout.title = title
        fab.setOnClickListener { view ->
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