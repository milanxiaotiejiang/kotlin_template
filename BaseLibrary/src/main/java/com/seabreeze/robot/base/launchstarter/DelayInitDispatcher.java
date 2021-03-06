package com.seabreeze.robot.base.launchstarter;

import android.os.Looper;
import android.os.MessageQueue;

import com.seabreeze.robot.base.launchstarter.task.DispatchRunnable;
import com.seabreeze.robot.base.launchstarter.task.Task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class DelayInitDispatcher {

    private Queue<Task> mDelayTasks = new LinkedList<>();

    private MessageQueue.IdleHandler mIdleHandler = () -> {
        if (mDelayTasks.size() > 0) {
            Task task = mDelayTasks.poll();
            new DispatchRunnable(task).run();
        }
        return !mDelayTasks.isEmpty();
    };

    public DelayInitDispatcher addTask(Task task) {
        mDelayTasks.add(task);
        return this;
    }

    public void start() {
        Looper.myQueue().addIdleHandler(mIdleHandler);
    }

}
