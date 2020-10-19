package com.seabreeze.robot.data.net.ok

import android.util.Log
import com.elvishew.xlog.XLog
import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * User: milan
 * Time: 2019/12/23 14:38
 * Des: okHttp 监听
 */
class OkHttpEventListener : EventListener() {

    private val okHttpEvent: OkHttpEvent = OkHttpEvent()

    override fun callStart(call: Call) {
        super.callStart(call)
        XLog.i("callStart")
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        okHttpEvent.dnsStartTime = System.currentTimeMillis()
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        okHttpEvent.dnsEndTime = System.currentTimeMillis()
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        okHttpEvent.responseBodySize = byteCount
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        okHttpEvent.apiSuccess = true
    }

    override fun callFailed(call: Call, ioe: IOException) {
        XLog.i("callFailed ")
        super.callFailed(call, ioe)
        okHttpEvent.apiSuccess = false
        okHttpEvent.errorReason = Log.getStackTraceString(ioe)
        XLog.i("reason " + okHttpEvent.errorReason)
    }

    companion object {
        @JvmField
        val FACTORY: Factory = object : Factory {
            override fun create(call: Call): EventListener {
                return OkHttpEventListener()
            }
        }
    }

}