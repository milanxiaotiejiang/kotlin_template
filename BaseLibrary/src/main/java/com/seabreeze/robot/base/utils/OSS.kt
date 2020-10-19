package com.seabreeze.robot.base.utils

import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.common.AppContext
import com.seabreeze.robot.base.ext.currentTimeMillis
import com.seabreeze.robot.base.model.Upload
import kotlinx.coroutines.*
import java.io.File
import java.util.*

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/30
 * @description : milanxiaotiejiang
 * </pre>
 */
private val long = System.currentTimeMillis()

fun main() {
    runBlocking {
        val processAllPages = processAllPages()
        println("processAllPages $processAllPages")
        println("完成 ${System.currentTimeMillis() - long}")
    }
}

suspend fun processAllPages() = withContext(Dispatchers.IO) {
    println("开始 ${System.currentTimeMillis() - long}")
    val jobs = mutableListOf<Int>()
    val job1 = async {
        processPages(1)
    }
    val job2 = async {
        processPages(2)
    }
    val job3 = async {
        processPages(3)
    }
//    jobs.add(job1)
//    jobs.add(job2)
//    jobs.add(job3)

    val await1 = job1.await()
    val await2 = job2.await()
    val await3 = job3.await()
    println("$await1 $await2 $await3")
    jobs.add(await1)
    jobs.add(await2)
    jobs.add(await3)
    jobs
}

fun processPages(number: Int): Int {
    println("number $number ${System.currentTimeMillis() - long}")
    Thread.sleep((1000 * number).toLong())
    println("number $number ${System.currentTimeMillis() - long}")
    return number * 33
}

class OSS private constructor() {

    private lateinit var ossClient: OSSClient

    private fun initOSS(
        accessKeyId: String,
        accessKeySecret: String,
        endPoint: String,
        token: String
    ) {
        // 在移动端建议使用STS的方式初始化OSSClient。
        val credentialProvider = OSSStsTokenCredentialProvider(
            accessKeyId,
            accessKeySecret,
            token
        )
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒。
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒。
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个。
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次。
        ossClient = OSSClient(AppContext, endPoint, credentialProvider, conf)
    }


    suspend fun uploadMultipleFile(
        upload: Upload,
        files: List<String>,
    ): MutableList<UploadResult> {
        if (!this@OSS::ossClient.isInitialized) {
            initOSS(
                upload.accessKeyId,
                upload.accessKeySecret,
                upload.region + ".aliyuncs.com",
                upload.securityToken
            )
        }

        return withContext(Dispatchers.IO) {

            val deferredList = mutableListOf<Deferred<UploadResult>>()
            val currentTimeMillis1 = currentTimeMillis
            files.forEach { path ->
                val deferred = async {
                    val file = File(path)
                    val result = UploadResult(file.name)
                    if (file.exists()) {
                        // 构造上传请求。
                        val putObjectRequest =
                            PutObjectRequest(upload.bucket, file.name, file.absolutePath)
                        val putObject = ossClient.putObject(putObjectRequest)
                        if (putObject.statusCode == 200) {
                            val url = ossClient.presignConstrainedObjectURL(
                                upload.bucket, file.name,
                                Date(Date().time + 3600L * 1000 * 24 * 365 * 10).time
                            )
                                .split("\\?".toRegex())
                                .toTypedArray()[0]
                            result.ossUrl = url
                        } else {
                            result.error = UploadThrowable("statusCode is ${putObject.statusCode}")
                        }
                    } else {
                        result.error = UploadThrowable("file is not exists")
                    }
                    result
                }
                deferredList.add(deferred)
            }
            val resultList = mutableListOf<UploadResult>()
            deferredList.forEach {
                resultList.add(it.await())
            }
            XLog.e(currentTimeMillis - currentTimeMillis1)
            resultList
        }
    }

    fun uploadSingleFile(
        upload: Upload,
        file: File,
        success: (errorMsg: String) -> Unit,
        failure: (errorMsg: String) -> Unit = { _ -> },
        progress: (request: PutObjectRequest, currentSize: Long, totalSize: Long) -> Unit = { _, _, _ -> }
    ) {
        if (!this@OSS::ossClient.isInitialized) {
            initOSS(
                upload.accessKeyId,
                upload.accessKeySecret,
                upload.region + ".aliyuncs.com",
                upload.securityToken
            )
        }

        // 构造上传请求。
        val putObjectRequest = PutObjectRequest(upload.bucket, file.name, file.absolutePath)
        // 异步上传时可以设置进度回调。
        putObjectRequest.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
            progress(request, currentSize, totalSize)
        }
        ossClient.asyncPutObject(
            putObjectRequest,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                    // 设置URL过期时间为10年  3600l* 1000*24*365*10
                    val expiration = Date(Date().time + 3600L * 1000 * 24 * 365 * 10)
                    // 生成URL
                    val url =
                        ossClient.presignConstrainedObjectURL(
                            upload.bucket,
                            file.name,
                            expiration.time
                        ).split("\\?".toRegex()).toTypedArray()[0]
                    success(url)
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    failure(clientException?.message + " " + serviceException?.rawMessage)
                }

            })
    }

    companion object {
        val INSTANCE: OSS by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OSS()
        }
    }
}

class UploadThrowable(errorMsg: String) : Throwable(errorMsg)
data class UploadResult(
    val key: String,
    var ossUrl: String? = null,
    var error: UploadThrowable? = null
)