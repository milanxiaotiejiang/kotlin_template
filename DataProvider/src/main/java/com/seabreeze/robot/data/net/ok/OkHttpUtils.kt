package com.seabreeze.robot.data.net.ok

import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.*

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object OkHttpUtils {
    fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
        try {
            if (bksFile == null || password == null) return null
            val clientKeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            kmf.init(clientKeyStore, password.toCharArray())
            return kmf.keyManagers
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun prepareTrustManager(vararg certificates: InputStream?): Array<TrustManager>? {
        if (certificates.isEmpty()) return null
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            // 创建一个默认类型的KeyStore，存储我们信任的证书
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            for ((index, certStream) in certificates.withIndex()) {
                val certificateAlias = index.toString()
                // 证书工厂根据证书文件的流生成证书 cert
                val cert = certificateFactory.generateCertificate(certStream)
                // 将 cert 作为可信证书放入到keyStore中
                keyStore.setCertificateEntry(certificateAlias, cert)
                try {
                    certStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //我们创建一个默认类型的TrustManagerFactory
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书
            tmf.init(keyStore)
            //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书
            return tmf.trustManagers
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }
}