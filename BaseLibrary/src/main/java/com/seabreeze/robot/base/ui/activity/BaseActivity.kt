package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.AppManager
import com.seabreeze.robot.base.common.Settings.project_theme
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.setScreenPortrait
import com.seabreeze.robot.base.ui.ProgressDialogFragment
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des:
 */
open class BaseActivity : RxAppCompatActivity(), BGASwipeBackHelper.Delegate {

    private lateinit var mSwipeBackHelper: BGASwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        //主题适配
        setTheme(project_theme)
        super.onCreate(savedInstanceState)
        setScreenPortrait()
        //滑动返回
        initSwipeBackFinish()
        AppManager.addActivity(this)
        if (booHideBottom()) {
            //隐藏虚拟按键，并且全屏
            val window = window
            val params = window.attributes
            params.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            window.attributes = params
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.finishActivity(this)
    }

    //获取Window中视图content
    val contentView: View
        get() {
            val content = find<FrameLayout>(android.R.id.content)
            return content.getChildAt(0)
        }

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding) {
            return
        }
        mSwipeBackHelper.backward()
    }

    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
    }

    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {
        XLog.i("onSwipeBackLayoutSlide $slideOffset")
    }

    override fun onSwipeBackLayoutCancel() {
        XLog.i("onSwipeBackLayoutCancel")
    }

    override fun isSupportSwipeBack() = true

    protected open fun booHideBottom() = false

    protected open fun isImmersionBar() = true

    open fun setStatusBar(view: View) {
        val statusHeight = getStatusBarHeight()
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            statusHeight
        )
    }

    private fun getStatusBarHeight(): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            return resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private lateinit var progressDialogFragment: ProgressDialogFragment

    /**
     * 显示加载(转圈)对话框
     */
    fun showProgressDialog(message: String) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            progressDialogFragment.show(supportFragmentManager, message, false)
        }
    }

    /**
     * 隐藏加载(转圈)对话框
     */
    fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {
            progressDialogFragment.dismissAllowingStateLoss()
        }
    }
}
