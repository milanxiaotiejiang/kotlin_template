package com.seabreeze.robot.data.net.ok

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * User: milan
 * Time: 2019/8/23 2:12
 * Des:
 */
class SafeHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }
}