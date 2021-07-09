package com.seabreeze.robot.data.net.asm;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import com.elvishew.xlog.XLog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.EventListener;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public class CustomGlobalEventListener extends EventListener {

    public static final EventListener.Factory FACTORY = new EventListener.Factory() {

        final AtomicLong nextCallId = new AtomicLong(1L);

        @Override
        public EventListener create(@NotNull Call call) {
            long callId = nextCallId.getAndIncrement();
            return new CustomGlobalEventListener(callId);
        }

    };

    private final long callId;
    private long callStartNanos = 0L;
    private boolean isNewConnection = false;

    public CustomGlobalEventListener(long callId) {
        this.callId = callId;
    }

    @Override
    public void callStart(@NotNull Call call) {
        callStartNanos = SystemClock.elapsedRealtime();
    }

    @Override
    public void connectStart(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy) {
        isNewConnection = true;
    }

    @Override
    public void callEnd(@NotNull Call call) {
        printResult(true, call);
    }

    @Override
    public void callFailed(@NotNull Call call, @NotNull IOException ioe) {
        printResult(false, call);
    }

    @SuppressLint("DefaultLocale")
    private void printResult(boolean success, Call call) {
        float elapsed = (float) ((SystemClock.elapsedRealtime() - callStartNanos) / 1000.0);
        String from = isNewConnection ? "newest connection" : "pooled connection";
        String url = call.request().url().toString();
        String result = String.format("%04d %s Call From %s costs %.3f, url %s", callId, success ? "Success" : "Fail", from, elapsed, url);
        XLog.i(result);
    }

}
