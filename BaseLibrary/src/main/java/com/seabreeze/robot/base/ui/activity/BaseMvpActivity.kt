package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.elvishew.xlog.XLog
import com.gyf.immersionbar.ImmersionBar
import com.ldoublem.loadingviewlib.view.LVCircularSmile
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.postEvent
import com.seabreeze.robot.base.ext.toast
import com.seabreeze.robot.base.model.TokenInvalidEvent
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.presenter.view.BaseView
import com.seabreeze.robot.base.router.startMain
import kotlinx.android.synthetic.main.loading.*
import retrofit2.HttpException
import java.lang.reflect.ParameterizedType

/*
    Activity基类，业务相关
 */
abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>> :
    InternationalizationActivity(), BaseView<Presenter> {

    final override val mPresenter: Presenter

    init {
        mPresenter = findPresenterClass().newInstance()
        mPresenter.mView = this
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<Presenter>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    private lateinit var mLoadingDialog: MaterialDialog

    private lateinit var loadView: View
    private lateinit var lvCircularSmile: LVCircularSmile
    private lateinit var loadTip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter.lifecycleProvider = this
        //初始加载框
        loadView = layoutInflater.inflate(R.layout.loading, null)
        lvCircularSmile = loadView.find(R.id.lVCircularSmile)
        loadTip = loadView.find(R.id.loadTip)
        mLoadingDialog = MaterialDialog(this, ModalDialog)
            .customView(view = loadView, dialogWrapContent = true)

        //ARouter注册
        ARouter.getInstance().inject(this)

        setLayout()

        if (isImmersionBar()) {
            setImmersionBar()
        }

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

    protected abstract fun initData()

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    override fun showToast(msg: String) {
        XLog.e(msg)
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        runOnUiThread { toast { msg } }
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        if (!mLoadingDialog.isShowing) {
            mLoadingDialog.show {
                title(text = title)
                lifecycleOwner(this@BaseMvpActivity)
                loadTip.text = tip
                lVCircularSmile.setViewColor(color)
                lvCircularSmile.startAnim()
            }
        }
    }

    override fun hideLoading() {
        lvCircularSmile.stopAnim()
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
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
