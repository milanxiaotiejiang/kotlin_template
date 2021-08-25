package com.seabreeze.library.okhttp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.Interceptor;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public class OkHttpHooker {

    public static EventListener.Factory globalEventFactory = new EventListener.Factory() {
        @Override
        @NotNull
        public EventListener create(@NotNull Call call) {
            return EventListener.NONE;
        }
    };

    public static Dns globalDns = Dns.SYSTEM;

    public static List<Interceptor> globalInterceptors = new ArrayList<>();

    public static List<Interceptor> globalNetworkInterceptors = new ArrayList<>();

    public static void installEventListenerFactory(EventListener.Factory factory) {
        globalEventFactory = factory;
    }

    public static void installDns(Dns dns) {
        globalDns = dns;
    }

    public static void installInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            globalInterceptors.add(interceptor);
        }
    }

    public static void installNetworkInterceptors(Interceptor networkInterceptor) {
        if (networkInterceptor != null) {
            globalNetworkInterceptors.add(networkInterceptor);
        }
    }

}
