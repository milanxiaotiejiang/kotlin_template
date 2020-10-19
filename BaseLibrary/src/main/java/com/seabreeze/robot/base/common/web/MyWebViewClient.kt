package com.seabreeze.robot.base.common.web

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*

/**
 * User: milan
 * Time: 2020/4/16 16:16
 * Des:
 */
class MyWebViewClient(val context: Context, private val key: Int) : WebViewClient() {

    private lateinit var receivedError: (error: MyWebResourceError) -> Unit

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let { view?.loadUrl(it) }
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        BroadCastManager.onPageStarted(context, key, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        BroadCastManager.onPageFinished(context, key, url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (this::receivedError.isInitialized) {
            error?.let {
                receivedError.invoke(MyWebResourceError(it, it.transformation()))
            }
        }
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//        super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }

    fun setReceivedError(receivedError: (error: MyWebResourceError) -> Unit) {
        this.receivedError = receivedError
    }

}