package com.seabreeze.robot.data

import androidx.room.Room
import com.seabreeze.robot.base.common.BaseApplication
import com.seabreeze.robot.data.db.AppDatabase
import com.seabreeze.robot.data.db.MIGRATION_1_2
import com.seabreeze.robot.data.net.DataRepository
import com.seabreeze.robot.data.net.RetrofitFactory
import com.seabreeze.robot.data.net.ok.OkHttpManager
import okhttp3.OkHttpClient

/**
 * User: milan
 * Time: 2020/4/9 11:19
 * Des:
 */
open class DataApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        dataApp = this
    }

    companion object {

        private lateinit var dataApp: DataApplication

        val appDatabase: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(dataApp, AppDatabase::class.java, "tao")
                .fallbackToDestructiveMigration()
                .enableMultiInstanceInvalidation()//跨进程
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        val okHttpClient: OkHttpClient by lazy {
            OkHttpManager.INSTANCE.initOkHttpClient(dataApp)
        }

        val retrofitFactory: RetrofitFactory by lazy { RetrofitFactory.getInstance(okHttpClient) }

        val dataRepository: DataRepository by lazy { DataRepository.INSTANCE }
    }

}
