package com.seabreeze.robot.base.launchstarter.task;

import android.os.Looper;
import android.os.Process;

import androidx.core.os.TraceCompat;

import com.elvishew.xlog.XLog;
import com.seabreeze.robot.base.launchstarter.TaskDispatcher;
import com.seabreeze.robot.base.launchstarter.stat.TaskStat;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class DispatchRunnable implements Runnable {
    private Task mTask;
    private TaskDispatcher mTaskDispatcher;

    public DispatchRunnable(Task task) {
        this.mTask = task;
    }

    public DispatchRunnable(Task task, TaskDispatcher dispatcher) {
        this.mTask = task;
        this.mTaskDispatcher = dispatcher;
    }

    @Override
    public void run() {
        TraceCompat.beginSection(mTask.getClass().getSimpleName());
        XLog.i(mTask.getClass().getSimpleName()
                + " begin run" + "  Situation  " + TaskStat.getCurrentSituation());

        Process.setThreadPriority(mTask.priority());

        long startTime = System.currentTimeMillis();

        mTask.setWaiting(true);
        mTask.waitToSatisfy();

        long waitTime = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        // 执行Task
        mTask.setRunning(true);
        mTask.run();

        // 执行Task的尾部任务
        Runnable tailRunnable = mTask.getTailRunnable();
        if (tailRunnable != null) {
            tailRunnable.run();
        }

        if (!mTask.needCall() || !mTask.runOnMainThread()) {
//            printTaskLog(startTime, waitTime);

            TaskStat.markTaskDone();
            mTask.setFinished(true);
            if (mTaskDispatcher != null) {
                mTaskDispatcher.satisfyChildren(mTask);
                mTaskDispatcher.markTaskDone(mTask);
            }
            XLog.i(mTask.getClass().getSimpleName() + " finish");
        }
        TraceCompat.endSection();
    }

    /**
     * 打印出来Task执行的日志
     *
     * @param startTime
     * @param waitTime
     */
    private void printTaskLog(long startTime, long waitTime) {
        long runTime = System.currentTimeMillis() - startTime;
        XLog.i(mTask.getClass().getSimpleName() + "  wait " + waitTime + "    run "
                + runTime + "   isMain " + (Looper.getMainLooper() == Looper.myLooper())
                + "  needWait " + (mTask.needWait() || (Looper.getMainLooper() == Looper.myLooper()))
                + "  ThreadId " + Thread.currentThread().getId()
                + "  ThreadName " + Thread.currentThread().getName()
                + "  Situation  " + TaskStat.getCurrentSituation()
        );
    }

}
