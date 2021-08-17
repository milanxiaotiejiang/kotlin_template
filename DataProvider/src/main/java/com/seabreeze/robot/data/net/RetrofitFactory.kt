package com.seabreeze.robot.data.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitFactory private constructor(client: OkHttpClient) {

    companion object {

        const val API_ROBOT_MALL = "https://www.wanandroid.com/"

        @Volatile
        private var instance: RetrofitFactory? = null

        fun getInstance(client: OkHttpClient): RetrofitFactory {
            if (instance == null) {
                synchronized(RetrofitFactory::class) {
                    if (instance == null) {
                        instance = RetrofitFactory(client)
                    }
                }
            }
            return instance!!
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_ROBOT_MALL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    /*
        具体服务实例化
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}
