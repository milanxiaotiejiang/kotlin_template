package com.thirtydays.kotlin.mvvm.vm

import androidx.lifecycle.MutableLiveData
import com.seabreeze.robot.base.ext.coroutine.launchUI
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.model.ListModel
import com.seabreeze.robot.base.widget.loadpage.LoadPageStatus
import com.seabreeze.robot.data.net.bean.response.CommodityPO
import com.thirtydays.kotlin.mvvm.repository.CommodityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : CommodityViewModel
 * </pre>
 */
class CommodityViewModel : BaseViewModel() {

    private val mRepository: CommodityRepository = CommodityRepository()

    val listModelLiveData: MutableLiveData<ListModel<CommodityPO>> by lazy {
        MutableLiveData<ListModel<CommodityPO>>()
    }

    /**
     * 刷新加载布局常用封装
     */
    val loadPageLiveData: MutableLiveData<LoadPageStatus> by lazy {
        MutableLiveData<LoadPageStatus>()
    }

    fun commodity(isRefresh: Boolean = false, currentTotal: Int = 0) =
        launchUI {
            withContext(Dispatchers.IO) {
                mRepository.commodity(isRefresh, currentTotal, listModelLiveData, loadPageLiveData)
            }
        }

}