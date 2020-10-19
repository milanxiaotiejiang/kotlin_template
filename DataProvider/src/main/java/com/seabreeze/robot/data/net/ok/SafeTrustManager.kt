package com.seabreeze.robot.data.net.ok

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 * User: milan
 * Time: 2019/8/23 2:12
 * Des:
 */
class SafeTrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        try {
            for (certificate in chain) {
                certificate.checkValidity()
            }
        } catch (e: Exception) {
            throw CertificateException(e)
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }
}