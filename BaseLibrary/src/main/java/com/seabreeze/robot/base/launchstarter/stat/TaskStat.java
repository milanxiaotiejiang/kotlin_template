package com.seabreeze.robot.base.launchstarter.stat;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class TaskStat {

    private static volatile String sCurrentSituation = "";
    private static List<TaskStatBean> sBeans = new ArrayList<>();
    private static AtomicInteger sTaskDoneCount = new AtomicInteger();
    private static boolean sOpenLaunchStat = false;// 是否开启统计


    public static String getCurrentSituation() {
        return sCurrentSituation;
    }

    public static void setCurrentSituation(String currentSituation) {
        if (!sOpenLaunchStat) {
            return;
        }
        XLog.i("currentSituation   " + currentSituation);
        sCurrentSituation = currentSituation;
        setLaunchStat();
    }

    public static void markTaskDone() {
        sTaskDoneCount.getAndIncrement();
    }

    public static void setLaunchStat() {
        TaskStatBean bean = new TaskStatBean();
        bean.setSituation(sCurrentSituation);
        bean.setCount(sTaskDoneCount.get());
        sBeans.add(bean);
        sTaskDoneCount = new AtomicInteger(0);
    }

}
