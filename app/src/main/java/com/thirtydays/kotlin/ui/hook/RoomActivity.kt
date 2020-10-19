package com.thirtydays.kotlin.ui.hook

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ui.activity.BaseMvvmActivity
import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.base.vm.BaseViewModel
import com.seabreeze.robot.data.DataApplication.Companion.appDatabase
import com.seabreeze.robot.data.db.bean.SystemCount
import com.thirtydays.kotlin.R
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RoomViewModel : BaseViewModel<RoomRepository>() {
    override fun createRepository() = RoomRepository()

    val countLiveData: MutableLiveData<List<SystemCount>> by lazy {
        MutableLiveData<List<SystemCount>>()
    }

    fun getSystemCount(count: SystemCount) {
        launch {//测试立即返回后数据是否接收到（内存泄漏）

            XLog.e("当前线程是否是主线程 1 : ${Looper.myLooper() == Looper.getMainLooper()}")
            val systemCount = mRepository.getSystemCount(count)

            XLog.e("当前线程是否是主线程 3 : ${Looper.myLooper() == Looper.getMainLooper()}")
            countLiveData.value = systemCount
        }
    }

    fun testLike(title: String) {
        launch {
            val list = mRepository.testLike(title)
            XLog.e(list)
        }
    }
}

class RoomRepository : BaseRepository() {
    suspend fun getSystemCount(count: SystemCount): List<SystemCount> {
        return withContext(Dispatchers.IO) {
            XLog.e("当前线程是否是主线程 2 : ${Looper.myLooper() == Looper.getMainLooper()}")
            delay(1500)
            appDatabase.systemCount().insert(count)
            delay(500)
            appDatabase.systemCount().loadAll()
        }
    }

    suspend fun testLike(title: String): List<SystemCount> {
        return withContext(Dispatchers.IO) {
            appDatabase.systemCount().queryLikeByKey(title)
        }
    }
}

class RoomActivity : BaseMvvmActivity<RoomRepository, RoomViewModel>() {
    override fun createViewModel() = ViewModelProvider(this)[RoomViewModel::class.java]

    override fun getLayoutId() = R.layout.activity_room

    override fun initViewModel() {
        mViewModel.countLiveData.observe(this, Observer<List<SystemCount>> { t ->
            t?.let {
                XLog.e("size : ${it.size}")
            }
        })
    }

    override fun initData() {
        tvMvvm.setOnClickListener {
            val systemCount = SystemCount(System.currentTimeMillis().toString(), 1)
            mViewModel.getSystemCount(systemCount)
        }

        tvLike.setOnClickListener {
            mViewModel.testLike(System.currentTimeMillis().toString().substring(0, 5))
        }
    }


}



