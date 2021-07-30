package com.thirtydays.kotlin.ui.hook

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.framework.mvvm.BaseRepository
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.ui.activity.BaseVmActivity
import com.seabreeze.robot.data.DataApplication.Companion.appDatabase
import com.seabreeze.robot.data.db.bean.SystemCount
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.databinding.ActivityRoomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RoomViewModel : BaseViewModel() {
    val mRepository = RoomRepository()

    val countLiveData: MutableLiveData<List<SystemCount>> by lazy {
        MutableLiveData<List<SystemCount>>()
    }

    fun clickMvvm() {
        val systemCount = SystemCount(System.currentTimeMillis().toString(), 1)
        launch {//测试立即返回后数据是否接收到（内存泄漏）

            XLog.e("当前线程是否是主线程 1 : ${Looper.myLooper() == Looper.getMainLooper()}")
            val systemCount = mRepository.getSystemCount(systemCount)

            XLog.e("当前线程是否是主线程 3 : ${Looper.myLooper() == Looper.getMainLooper()}")
            countLiveData.value = systemCount
        }
    }

    fun clickLike() {
        launch {
            val list = mRepository.testLike(System.currentTimeMillis().toString().substring(0, 5))
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

class RoomActivity : BaseVmActivity<RoomViewModel, ActivityRoomBinding>(R.layout.activity_room) {


    override fun onInitDataBinding() {
        mDataBinding.viewModel = mViewModel
        mViewModel.run {
            countLiveData.observe(this@RoomActivity, { t ->
                t?.let {
                    XLog.e("size : ${it.size}")
                }
            })
        }
    }

    override fun initData() {

    }


}



