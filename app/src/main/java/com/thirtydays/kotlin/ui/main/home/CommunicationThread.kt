package com.graves.rubbishbag

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.elvishew.xlog.XLog
import com.graves.rubbishbag.CommunicationListenerHandler.Messages.Companion.ENDED
import com.graves.rubbishbag.CommunicationListenerHandler.Messages.Companion.INITIALISATION_FAILED
import com.graves.rubbishbag.CommunicationListenerHandler.Messages.Companion.INTERRUPTED_EXCEPTION
import com.graves.rubbishbag.CommunicationListenerHandler.Messages.Companion.IO_EXCEPTION
import com.graves.rubbishbag.CommunicationListenerHandler.Messages.Companion.READY
import com.seabreeze.robot.base.ext.foundation.DispatcherExecutor
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/20
 * @description : TODO
</pre> *
 */

fun startCommunicationThread() {
    val communicationListener: CommunicationListener = object : CommunicationListener {
        override fun onCommunicationReady(thread: CommunicationThread) {
            XLog.e("onCommunicationReady")
            val cd = CountDownLatch(3)
            Thread {
                for (index in 0..20) {
                    thread.sendData("1 $index")
                    Thread.sleep(100L)
                }
                cd.countDown()
            }.start()
            Thread {
                for (index in 0..10) {
                    thread.sendData("2 $index")
                    Thread.sleep(200L)
                }
                cd.countDown()
            }.start()
            Thread {
                for (index in 0..30) {
                    thread.sendData("3 $index")
                    Thread.sleep(100L)
                }
                cd.countDown()
            }.start()

            cd.await()
            XLog.e("onCommunicationReady finish")
        }

        override fun onCommunicationFailed(thread: CommunicationThread, error: CommunicationError) {
            XLog.e("onCommunicationFailed $error")
        }

        override fun onCommunicationEnded(thread: CommunicationThread) {
            XLog.e("onCommunicationEnded")
        }
    }
    val communicationThread = CommunicationThread(communicationListener)
    DispatcherExecutor.iOExecutor.execute(communicationThread)

}

class CommunicationThread(listener: CommunicationListener) : Thread() {
    private val linkedBlockingQueue = LinkedBlockingQueue<String>()
    private val mListenerHandler: CommunicationListenerHandler

    @Volatile
    private var isRunning = false

    init {
        name = "$NAME $id"
        mListenerHandler = CommunicationListenerHandler(listener)
    }

    override fun run() {
        super.run()

        //判断
//        if (okHttpClient == null) {
//            sendMessage(INITIALISATION_FAILED)
//            return
//        }

        listenStream()
        endConnection()
    }

    private fun listenStream() {
        isRunning = true
        sendMessage(READY)
        while (isRunning) {
            try {
                val take = linkedBlockingQueue.take()

                //doSomeThing()
                sleep(200L)
                XLog.e(take)

            } catch (e: InterruptedException) {
                e.printStackTrace()
                sendMessage(INTERRUPTED_EXCEPTION)
            } catch (e: IOException) {
                e.printStackTrace()
                sendMessage(IO_EXCEPTION)
            }
        }
    }

    private fun endConnection() {
        isRunning = false
        sendMessage(ENDED)
        interrupt()
    }

    fun sendData(value: String) {
        linkedBlockingQueue.add(value)
    }

    fun cancel() {
        if (isRunning) {
            endConnection()
        }
    }

    private fun sendMessage(@CommunicationListenerHandler.Messages message: Int) {
        mListenerHandler.obtainMessage(message, this).sendToTarget()
    }

    companion object {
        private const val NAME = "CommunicationThread"
    }
}

interface CommunicationListener {
    fun onCommunicationReady(thread: CommunicationThread)
    fun onCommunicationFailed(
        thread: CommunicationThread,
        error: CommunicationError
    )

    fun onCommunicationEnded(thread: CommunicationThread)
}

enum class CommunicationError {
    INITIALISATION_FAILED, IO_EXCEPTION, INTERRUPTED_EXCEPTION
}

class CommunicationListenerHandler(listener: CommunicationListener) :
    Handler(Looper.getMainLooper()) {
    private val mReference: WeakReference<CommunicationListener> = WeakReference(listener)

    @Retention(AnnotationRetention.SOURCE)
    annotation class Messages {
        companion object {
            var READY = 0
            var ENDED = 1
            var INITIALISATION_FAILED = 2
            var IO_EXCEPTION = 3
            var INTERRUPTED_EXCEPTION = 4
        }
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val listener = mReference.get()
        if (listener != null) {
            @Messages val what = msg.what
            val thread = msg.obj as CommunicationThread
            when (what) {
                READY -> listener.onCommunicationReady(thread)
                ENDED -> listener.onCommunicationEnded(thread)
                INITIALISATION_FAILED -> listener.onCommunicationFailed(
                    thread,
                    CommunicationError.INITIALISATION_FAILED
                )
                IO_EXCEPTION -> listener.onCommunicationFailed(
                    thread,
                    CommunicationError.IO_EXCEPTION
                )
                INTERRUPTED_EXCEPTION -> listener.onCommunicationFailed(
                    thread,
                    CommunicationError.INTERRUPTED_EXCEPTION
                )
            }
        }
    }

}
