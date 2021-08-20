package com.graves.rubbishbag

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/20
 * @description : TODO
</pre> *
 */
class ThreadSynchronized {

    val startTime = System.currentTimeMillis()

    @Before
    fun before() {
        println(startTime)
    }

    @After
    fun after() {
        println("${System.currentTimeMillis() - startTime}")
    }

    @Test
    fun testSynchronized() {//3007
        lateinit var s1: String
        lateinit var s2: String
        Thread {
            synchronized(Unit) {
                s1 = task1()
            }
        }.start()
        s2 = task2()
        synchronized(Unit) {
            task3(s1, s2)
        }
    }

    @Test
    fun testReentrantLock() {//3025
        lateinit var s1: String
        lateinit var s2: String
        val lock = ReentrantLock()
        Thread {
            lock.lock()
            s1 = task1()
            lock.unlock()
        }.start()
        s2 = task2()
        lock.lock()
        task3(s1, s2)
        lock.unlock()

    }

    @Test
    fun testBlockingQueue() {//3017
        lateinit var s1: String
        lateinit var s2: String
        val queue = SynchronousQueue<Unit>()
        Thread {
            s1 = task1()
            queue.put(Unit)
        }.start()
        s2 = task2()
        queue.take()
        task3(s1, s2)
    }

    @Test
    fun testCountDownLatch() {//3013

        lateinit var s1: String
        lateinit var s2: String
        val cd = CountDownLatch(2)
        Thread {
            s1 = task1()
            cd.countDown()
        }.start()
        Thread {
            s2 = task2()
            cd.countDown()
        }.start()

        cd.await()
        task3(s1, s2)
    }

    @Test
    fun testCyclicBarrier() {//3016
        lateinit var s1: String
        lateinit var s2: String
        val cb = CyclicBarrier(3)
        Thread {
            s1 = task1()
            cb.await()
        }.start()
        Thread {
            s2 = task2()
            cb.await()
        }.start()

        cb.await()
        task3(s1, s2)
    }

    @Test
    fun testCas() {//3015
        lateinit var s1: String
        lateinit var s2: String
        val cas = AtomicInteger(2)
        Thread {
            s1 = task1()
            cas.getAndDecrement()
        }.start()
        Thread {
            s2 = task2()
            cas.getAndDecrement()
        }.start()

        while (cas.get() != 0) {
        }
        task3(s1, s2)
    }

    @Volatile
    var cnt = 2

    //错误
    @Test
    fun testVolatile() {//3003
        lateinit var s1: String
        lateinit var s2: String
        Thread {
            s1 = task1()
            cnt--
        }.start()

        Thread {
            s2 = task2()
            cnt--
        }.start()

        while (cnt != 0) {
        }
        task3(s1, s2)

    }

    @Test
    fun testFuture() {//3018
        val future1 = FutureTask {
            task1()
        }
        val future2 = FutureTask {
            task2()
        }
        Executors.newCachedThreadPool().execute(future1)
        Executors.newCachedThreadPool().execute(future2)
        task3(future1.get(), future2.get())
    }

    @Test
    fun testCompletableFuture() {//3021
        CompletableFuture.supplyAsync {
            task1()
        }
            .thenCombine(CompletableFuture.supplyAsync {
                task2()
            }) { p1, p2 ->
                task3(p1, p2)
            }.join()
    }

    @Test
    fun testRxjava() {//3252
        Observable.zip(
            Observable.fromCallable {
                task1()
            }.subscribeOn(Schedulers.newThread()),
            Observable.fromCallable {
                task2()
            }.subscribeOn(Schedulers.newThread()),
            { s1, s2 ->
                task3(s1, s2)
            }
        )
            .test()
            .awaitTerminalEvent()
    }

    @Test
    fun testCoroutine() {//3033
        runBlocking {
            val c1 = async(Dispatchers.IO) {
                task1()
            }
            val c2 = async(Dispatchers.IO) {
                task2()
            }
            task3(c1.await(), c2.await())
        }
    }

    @Test
    fun testFlow() {//3451
        val flow1 = flow { emit(task1()) }
        val flow2 = flow { emit(task2()) }

        runBlocking {
            flow1.zip(flow2) { t1, t2 ->
                task3(t1, t2)
            }
                .flowOn(Dispatchers.IO)
                .collect {
                }
        }
    }

    private fun task1(): String {
        println("task1 start")
        Thread.sleep(2000L)
        println("task1 end")
        return "task1"
    }

    private fun task2(): String {
        println("task2 start")
        Thread.sleep(3000L)
        println("task2 end")
        return "task2"
    }

    private fun task3(s1: String, s2: String) {
        println("$s1 $s2")
    }
}