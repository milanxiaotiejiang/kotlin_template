package com.seabreeze.robot.base.ext.foundation

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.AppApplication
import com.seabreeze.robot.base.common.AppManager.currentActivity
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import java.util.*

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/26
</pre> *
 */
const val TO_MAIN = "to_main"

fun Context.sendNotificationMessage(
    cls: Class<BaseActivity>,
    title: String?,
    message: String?,
) {
    //创建 intent
    val intent = Intent()
    //查找是否有 Activity 存在
    val topActivityOrApp = topActivityOrApp
    //设置跳转页面
    intent.setClass(this, cls)
    //判断 intent 是否可用
    if (!isIntentAvailable(intent)) {
        return
    }
    //不存在 Activity 的需要加 new_task
    if (topActivityOrApp !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(TO_MAIN, true)
    }
    //创建 PendingIntent
    val notifyId = Random(System.nanoTime()).nextInt()
    val pendingIntent =
        PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_ONE_SHOT)
    //定义 channelId
    val CHAT_CHANNEL_ID = "custom_default_channel"
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    //创建 Notification 栏
    val notification =
        loadNotification(CHAT_CHANNEL_ID, title, message, defaultSoundUri, pendingIntent)
    //得到 NotificationManager
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    //版本判断
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(CHAT_CHANNEL_ID, "消息通知", NotificationManager.IMPORTANCE_DEFAULT)
        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
    }
    //正式发送通知
    notificationManager.notify(notifyId, notification)
}

private fun Context.loadNotification(
    channelId: String,
    title: String?,
    text: String?,
    defaultSoundUri: Uri?,
    pendingIntent: PendingIntent?
): Notification? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.no_net)
            .setContentTitle(title)//getString(R.string.my_app_name)
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .build()
    } else {
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.no_net)
            .setContentTitle(title)//getString(R.string.my_app_name)
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .build()
            .apply {
                flags = Notification.PRIORITY_HIGH
            }
    }
}

private fun isIntentAvailable(intent: Intent): Boolean {
    return AppApplication
        .packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .size > 0
}

private val topActivityOrApp =
    if (AppApplication.isAppForeground()) {
        val topActivity = currentActivity()
        topActivity ?: AppApplication
    } else {
        AppApplication
    }

private fun Application.isAppForeground(): Boolean {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val info = am.runningAppProcesses
    if (info == null || info.size == 0) return false
    for (aInfo in info) {
        if (aInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            if (aInfo.processName == packageName) {
                return true
            }
        }
    }
    return false
}

fun BaseActivity.startLauncherActivity() =
    getLauncherComponentName(this)?.apply {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.component = this
        startActivity(intent)
    }

private fun getLauncherComponentName(context: Context): ComponentName? {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfoList = pm.queryIntentActivities(intent, 0)
    for (resolveInfo in resolveInfoList) {
        val pkgName = resolveInfo.activityInfo.applicationInfo.packageName
        if (pkgName.equals(context.packageName, ignoreCase = true)) {
            val activityInfo = resolveInfo.activityInfo
            return ComponentName(activityInfo.packageName, activityInfo.name)
        }
    }
    return null
}