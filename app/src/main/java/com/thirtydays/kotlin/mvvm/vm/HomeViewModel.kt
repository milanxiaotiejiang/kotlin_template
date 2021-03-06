package com.thirtydays.kotlin.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.seabreeze.robot.base.vm.BaseViewModel
import com.seabreeze.robot.data.DataSettings.token_app
import com.seabreeze.robot.data.net.bean.response.AccountPO
import com.thirtydays.kotlin.mvvm.repository.HomeRepository

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : HomeViewModel
 * </pre>
 */
class HomeViewModel : BaseViewModel<HomeRepository>() {
    override fun createRepository() = HomeRepository()

    val accountData: MutableLiveData<AccountPO> by lazy {
        MutableLiveData<AccountPO>()
    }

    fun login(email: String, password: String) {
        launch(show = true) {
            val login = mRepository.login(email, password)
            if (login.resultStatus) {
                token_app = login.resultData.accountToken
                accountData.value = login.resultData
            }
        }
    }

}