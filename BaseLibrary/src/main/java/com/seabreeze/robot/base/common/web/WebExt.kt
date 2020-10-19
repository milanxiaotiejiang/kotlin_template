package com.seabreeze.robot.base.common.web

import android.webkit.WebResourceError
import android.webkit.WebViewClient

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/27
 * @description : TODO
 * </pre>
 */

/*
/** 通用错误 */
public static final int ERROR_UNKNOWN = -1;
/** 服务器或代理主机名查找失败 */
public static final int ERROR_HOST_LOOKUP = -2;
/** 不支持的身份验证方案（非基本或摘要） */
public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
/** 服务器上的用户身份验证失败 */
public static final int ERROR_AUTHENTICATION = -4;
/** 代理上的用户身份验证失败 */
public static final int ERROR_PROXY_AUTHENTICATION = -5;
/** 无法连接到服务器 */
public static final int ERROR_CONNECT = -6;
/** 无法读取或写入服务器 */
public static final int ERROR_IO = -7;
/** 连接超时 */
public static final int ERROR_TIMEOUT = -8;
/** 重定向过多 */
public static final int ERROR_REDIRECT_LOOP = -9;
/** 不支持的URI方案 */
public static final int ERROR_UNSUPPORTED_SCHEME = -10;
/** 无法执行SSL握手 */
public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;
/** 网址格式错误 */
public static final int ERROR_BAD_URL = -12;
/** 通用文件错误 */
public static final int ERROR_FILE = -13;
/** 文件未找到 */
public static final int ERROR_FILE_NOT_FOUND = -14;
/** 在此加载期间的请求太多 */
public static final int ERROR_TOO_MANY_REQUESTS = -15;
 */
class MyWebResourceError(val error: WebResourceError, val msg: String)

fun WebResourceError.transformation(): String {
    when (errorCode) {
        WebViewClient.ERROR_UNKNOWN -> return "未知错误"

        WebViewClient.ERROR_HOST_LOOKUP -> return "网络未连接，请连接网络"//"服务器或代理主机名查找失败"

        WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME -> return "不支持的身份验证方案（非基本或摘要）"

        WebViewClient.ERROR_AUTHENTICATION -> return "服务器上的用户身份验证失败"

        WebViewClient.ERROR_PROXY_AUTHENTICATION -> return "代理上的用户身份验证失败"

        WebViewClient.ERROR_CONNECT -> return "无法连接到服务器"

        WebViewClient.ERROR_IO -> return "无法读取或写入服务器"

        WebViewClient.ERROR_TIMEOUT -> return "连接超时"

        WebViewClient.ERROR_REDIRECT_LOOP -> return "重定向过多"

        WebViewClient.ERROR_UNSUPPORTED_SCHEME -> return "不支持的URI方案"

        WebViewClient.ERROR_FAILED_SSL_HANDSHAKE -> return "无法执行SSL握手"

        WebViewClient.ERROR_BAD_URL -> return "网址格式错误"

        WebViewClient.ERROR_FILE -> return "通用文件错误"

        WebViewClient.ERROR_FILE_NOT_FOUND -> return "文件未找到"

        WebViewClient.ERROR_TOO_MANY_REQUESTS -> return "在此加载期间的请求太多"

        else -> return "未知错误"
    }
}

