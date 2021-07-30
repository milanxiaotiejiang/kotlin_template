package com.seabreeze.robot.base.ext.foundation

import java.lang.Thread.currentThread
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object DispatcherExecutor {

    private const val KEEP_ALIVE_SECONDS = 5

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = 2.coerceAtLeast((CPU_COUNT - 1).coerceAtMost(5))
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE

    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val sThreadFactory = DefaultThreadFactory()
    private val sHandler = RejectedExecutionHandler { r, _ ->
        // 一般不会到这里
        Executors.newCachedThreadPool().execute(r)
    }

    /**
     * 获取CPU线程池
     */
    var cPUExecutor: ThreadPoolExecutor
        private set

    /**
     * 获取IO线程池
     */
    var iOExecutor: ExecutorService
        private set

    init {
        cPUExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory, sHandler
        )
        cPUExecutor.allowCoreThreadTimeOut(true)
        iOExecutor = Executors.newCachedThreadPool(sThreadFactory)
    }


    /**
     * The default thread factory.
     */
    private class DefaultThreadFactory : ThreadFactory {
        private val group: ThreadGroup
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String

        init {
            val s = System.getSecurityManager()
            group = if (s != null) s.threadGroup else currentThread().threadGroup!!
            namePrefix = "TaskDispatcherPool-" + poolNumber.getAndIncrement() + "-Thread-"
        }

        override fun newThread(r: Runnable): Thread {
            val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
            if (t.isDaemon) t.isDaemon = false
            if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
            return t
        }

        companion object {
            private val poolNumber = AtomicInteger(1)
        }


    }

}