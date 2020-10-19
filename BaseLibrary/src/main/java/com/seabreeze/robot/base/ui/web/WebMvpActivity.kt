package com.seabreeze.robot.base.ui.web

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.web.BroadCastManager
import com.seabreeze.robot.base.common.web.MyWebChromeClient
import com.seabreeze.robot.base.common.web.MyWebViewClient
import com.seabreeze.robot.base.common.web.WebViewListener
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.setGone
import com.seabreeze.robot.base.ext.setVisible
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.presenter.view.BaseView
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_web.*

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/7
 * @description : 网页
 * </pre>
 */
abstract class WebMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>> :
    BaseMvpActivity<Presenter>(), BaseView<Presenter> {

    private val key = System.identityHashCode(this)
    protected val mWebViews = mutableListOf<WebViewListener>()

    protected var mWebView: WebView? = null
    private var noNetLayout: RelativeLayout? = null
    private var tvNet: TextView? = null

    override fun getLayoutId() = R.layout.activity_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        BroadCastManager(this, key, mWebViews)
        mWebView = find(R.id.webView)
        webSettings()

        noNetLayout?.setOnClickListener {
            webViewReload()
            mWebView?.reload()
            noNetLayout?.setGone()
            mWebView?.setVisible()
        }
    }

    open fun webViewReload() {
    }

    open fun webSettings() {
        mWebView?.apply {
            webChromeClient = MyWebChromeClient(this@WebMvpActivity, progressBar, key)
            val myWebViewClient = MyWebViewClient(this@WebMvpActivity, key)
                .apply {
                    setReceivedError {
                        XLog.e("${it.error.errorCode}  ${it.error.description}")
                        if (it.error.errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
                            noNetLayout?.setVisible()
                            setGone()
                        } else {
                            setVisible()
                            noNetLayout?.setGone()
                        }
                        tvNet?.text = it.msg
                    }
                }
            webViewClient = myWebViewClient

            //        setDownloadListener(downloadListener);
            val settings: WebSettings = settings
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            //设置可以访问文件
            settings.allowFileAccess = true
            settings.loadWithOverviewMode = true
            settings.javaScriptEnabled = true
            settings.setAppCacheEnabled(true)
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            settings.useWideViewPort = true
            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView?.apply {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            parent.let { (it as ViewGroup).removeView(webView) }
            destroy()
        }
    }

//    override fun finish() {
//        if (!webView.canGoBack()) {
//            super.finish()
//        } else {
//            webView.goBack()
//        }
//    }

    override fun onBackPressed() {
        mWebView?.apply {
            if (!canGoBack()) {
                super.onBackPressed()
            } else {
                goBack()
            }
        }?.let {
            super.onBackPressed()
        }
    }

}