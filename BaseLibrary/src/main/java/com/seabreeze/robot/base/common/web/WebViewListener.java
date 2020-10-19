package com.seabreeze.robot.base.common.web;

import com.elvishew.xlog.XLog;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public abstract class WebViewListener {

    public void onProgressChanged(int progress) {
        XLog.d("WebViewListener onProgressChanged " + progress);
    }

    public void onReceivedTitle(String title) {
        XLog.d("WebViewListener onReceivedTitle " + title);
    }

    public void onReceivedTouchIconUrl(String url, boolean precomposed) {
        XLog.d("WebViewListener onReceivedTouchIconUrl " + url);
    }

    public void onPageStarted(String url) {
        XLog.d("WebViewListener onPageStarted " + url);
    }

    public void onPageFinished(String url) {
        XLog.d("WebViewListener onPageFinished " + url);
    }

    public void onLoadResource(String url) {
        XLog.d("WebViewListener onLoadResource " + url);
    }

    public void onPageCommitVisible(String url) {
        XLog.d("WebViewListener onPageCommitVisible " + url);
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                String mimeType, long contentLength) {
        XLog.d("WebViewListener onDownloadStart " + url);
    }
}
