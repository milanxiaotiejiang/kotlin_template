package com.seabreeze.robot.base.ext.foundation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import me.drakeet.support.toast.ToastCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/28
 * </pre>
 */
class PermissionCompat {

    /**
     * 跳转至权限设置页面
     *
     * @param activity
     */
    fun goPermissionSet(activity: Activity) {
        val name = Build.MANUFACTURER
        when (name) {
            "HUAWEI" -> goHuaWeiManager(activity)
            "vivo" -> goVivoManager(activity)
            "OPPO" -> goOppoManager(activity)
            "Coolpad" -> goCoolpadManager(activity)
            "Meizu" -> goMeizuManager(activity)
            "Xiaomi" -> goXiaoMiManager(activity)
            "samsung" -> goSangXinManager(activity)
            "Sony" -> goSonyManager(activity)
            "LG" -> goLGManager(activity)
            else -> goIntentSetting(activity)
        }
    }


    private fun goLGManager(activity: Activity) {
        try {
            val intent = Intent(activity.packageName)
            val comp = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            intent.component = comp
            activity.startActivityForResult(intent, PERMISSION_COMPAT_REQUEST_CODE)
        } catch (e: Exception) {
            ToastCompat.makeText(activity, "跳转失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    private fun goSonyManager(activity: Activity) {
        try {
            val intent = Intent(activity.packageName)
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = comp
            activity.startActivityForResult(intent, PERMISSION_COMPAT_REQUEST_CODE)
        } catch (e: Exception) {
            ToastCompat.makeText(activity, "跳转失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    private fun goHuaWeiManager(activity: Activity) {
        try {
            val intent = Intent(activity.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: Exception) {
            ToastCompat.makeText(activity, "跳转失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    private fun getMiuiVersion(): String? {
        val propName = "ro.miui.ui.version.name"
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } finally {
            try {
                input?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return line
    }

    private fun goXiaoMiManager(activity: Activity) {
        val rom = getMiuiVersion()
        val intent = Intent()
        if ("V6" == rom || "V7" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", activity.packageName)
        } else if ("V8" == rom || "V9" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", activity.packageName)
        } else {
            goIntentSetting(activity)
        }
    }

    private fun goMeizuManager(activity: Activity) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", activity.packageName)
            activity.startActivityForResult(intent, PERMISSION_COMPAT_REQUEST_CODE)
        } catch (localActivityNotFoundException: ActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace()
            goIntentSetting(activity)
        }
    }

    private fun goSangXinManager(activity: Activity) {
        //三星4.3可以直接跳转
        goIntentSetting(activity)
    }

    private fun goIntentSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        try {
            activity.startActivityForResult(intent, PERMISSION_COMPAT_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goOppoManager(activity: Activity) {
        doStartApplicationWithPackageName("com.coloros.safecenter", activity)
    }

    private fun goCoolpadManager(activity: Activity) {
        doStartApplicationWithPackageName("com.yulong.android.security:remote", activity)
    }

    private fun goVivoManager(activity: Activity) {
        doStartApplicationWithPackageName("com.bairenkeji.icaller", activity)
    }

    private fun doStartApplicationWithPackageName(
        tripartitePackageName: String,
        activity: Activity
    ) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = activity.packageManager.getPackageInfo(tripartitePackageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)
        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveInfoList = activity.packageManager
            .queryIntentActivities(resolveIntent, 0)
        val resolveinfo = resolveInfoList.iterator().next()
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            val packageName = resolveinfo.activityInfo.packageName
            // 该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityName]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.component = cn
            try {
                activity.startActivityForResult(intent, PERMISSION_COMPAT_REQUEST_CODE)
            } catch (e: Exception) {
                goIntentSetting(activity)
                e.printStackTrace()
            }
        }
    }

    companion object {

        const val PERMISSION_COMPAT_REQUEST_CODE = 110

        val INSTANCE: PermissionCompat by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PermissionCompat()
        }
    }
}