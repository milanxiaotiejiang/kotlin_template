package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.elvishew.xlog.XLog
import com.gyf.immersionbar.ImmersionBar
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.Block
import com.seabreeze.robot.base.ext.postEvent
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.model.CoroutineState
import com.seabreeze.robot.base.model.TokenInvalidEvent
import com.seabreeze.robot.base.router.startMain
import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.base.vm.BaseViewModel
import com.seabreeze.robot.base.vm.ModelView
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class BaseMvvmActivity<out Repository : BaseRepository, out ViewModel : BaseViewModel<Repository>> :
    InternationalizationActivity(), ModelView {

    val mViewModel: ViewModel by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ARouter注册
        ARouter.getInstance().inject(this)

        setLayout()

        if (isImmersionBar()) {
            setImmersionBar()
        }

        initViewModelActions()
        initViewModel()
        initData()
    }

    protected open fun setLayout() {
        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }
    }

    open fun setImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .statusBarColor(android.R.color.white)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    protected fun immersionNavigationBar() {
        ImmersionBar.with(this)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    protected abstract fun getLayoutId(): Int

    private fun initViewModelActions() {
        mViewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {
                        showLoading()
                    }
                    CoroutineState.FINISH -> {
                        hideLoading()
                    }
                    CoroutineState.ERROR -> {
                        hideLoading()
                    }
                }
            }
        })
    }

    protected abstract fun initViewModel()

    protected abstract fun initData()

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    fun launch(block: Block) {
        lifecycleScope.launch {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun showToast(msg: String) {
        XLog.e(msg)
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        runOnUiThread { toast { msg } }
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        showProgressDialog(tip)
    }

    override fun hideLoading() {
        dismissProgressDialog()
    }

    override fun onError(throwable: Throwable) {
        XLog.e(throwable.message)
        hideLoading()
        throwable.message?.let { showToast(it) }
        if (throwable is HttpException) {
            when (throwable.code()) {
                401 -> {
                    startMain(true)
                    postEvent(TokenInvalidEvent(throwable))
                }
            }
        }
    }
}