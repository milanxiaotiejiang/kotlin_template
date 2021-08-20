package com.graves.rubbishbag;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/20
 * @description : TODO
 * </pre>
 */
public class ThreadResult {
    private int sum = 0;

    @Test
    public void main() {
//        ThreadRet threadRet = new ThreadRet();
//        threadRet.startTest();
//        startCall();
        startPool();
    }

    /**
     * 线程池 获取线程执行结果
     */
    private void startPool() {
        //线程池
        ExecutorService service = Executors.newSingleThreadExecutor();
        //定义Callable
        Callable<String> callable = () -> {
            //返回result
            return "hello world";
        };
        //返回Future，实际上是FutureTask实例
        Future<String> future = service.submit(callable);
        try {
            System.out.println(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * FutureTask 获取线程执行结果
     * 定义Callable，线程具体的工作在此处理，可以返回任意值。
     * 定义FutureTask，持有Callable 引用，并且指定泛型的具体类型，该类型决定了线程最终的返回类型。实际上就是将Callable.call()返回值强转为具体类型。
     * 最后构造Thread，并传入FutureTask，而FutureTask实现了Runnable。
     * 通过FutureTask 获取线程执行结果。
     */
    private void startCall() {
        //定义Callable，具体的线程处理在call()里进行
        Callable<String> callable = () -> {
            //返回result
            return "hello world";
        };

        //定义FutureTask,持有Callable 引用
        FutureTask<String> futureTask = new FutureTask(callable);

        //开启线程
        new Thread(futureTask).start();

        try {
            //获取结果
            String result = futureTask.get();
            System.out.println("result:" + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 原始方式 获取线程执行结果
     */
    private void startTest() {
        Thread t1 = new Thread(() -> {
            int a = 5;
            int b = 5;
            int c = a + b;
            //将结果赋予成员变量
            sum = c;
            System.out.println("c:" + c);
        });
        t1.start();

        try {
            //等待线程执行完毕
            t1.join();
            //执行过这条语句后，说明线程已将sum赋值
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sum:" + sum);
    }
}
