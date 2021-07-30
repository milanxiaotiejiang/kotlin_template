package com.seabreeze.robot.base.ext

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * User: milan
 * Time: 2020/4/9 16:22
 * Des:
 */

fun <T> Observable<T>.execute(): Observable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.execute(lifecycleProvider: LifecycleProvider<*>): Observable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(lifecycleProvider.bindToLifecycle())

fun <T> Flowable<T>.execute(): Flowable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.execute(lifecycleProvider: LifecycleProvider<*>): Flowable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(lifecycleProvider.bindToLifecycle())

fun <T> Single<T>.execute(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.execute(lifecycleProvider: LifecycleProvider<*>): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(lifecycleProvider.bindToLifecycle())

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)

inline fun <reified T : View> Fragment.find(@IdRes id: Int): T = view?.findViewById(id) as T

inline fun <reified T : View> Dialog.find(@IdRes id: Int): T = findViewById(id)

fun Application.initWebViewDataDirectory() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val processName = getProcessName()
        processName?.let {
            if (packageName != it) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }
}

private fun Application.getProcessName(): String? {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningAppProcesses = manager.runningAppProcesses
    for (runningAppProcessInfo in runningAppProcesses) {
        if (runningAppProcessInfo.pid == android.os.Process.myPid()) {
            return runningAppProcessInfo.processName;
        }
    }
    return null
}