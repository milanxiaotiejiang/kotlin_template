package com.thirtydays.kotlin.mvvm.repository

import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.data.DataApplication.Companion.dataRepository
import com.seabreeze.robot.base.model.BaseResult
import com.seabreeze.robot.data.net.bean.response.AccountPO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : HomeRepository
 * </pre>
 */
class HomeRepository : BaseRepository() {
    suspend fun login(email: String, password: String): BaseResult<AccountPO> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(1000)
            dataRepository.login(email, password)
        }
    }
}