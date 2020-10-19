package com.thirtydays.kotlin.mvvm.repository

import androidx.lifecycle.MutableLiveData
import com.seabreeze.robot.base.ext.coroutineRequest
import com.seabreeze.robot.base.ext.dcEither
import com.seabreeze.robot.base.model.Either
import com.seabreeze.robot.base.model.ListModel
import com.seabreeze.robot.base.vm.BaseRepository
import com.seabreeze.robot.base.widget.loadpage.LoadPageStatus
import com.seabreeze.robot.data.DataApplication.Companion.dataRepository
import com.seabreeze.robot.data.common.Common
import com.seabreeze.robot.data.net.bean.response.CommodityPO
import com.seabreeze.robot.data.net.bean.response.Pager
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/12
 * @description : CommodityRepository
 * </pre>
 */
class CommodityRepository : BaseRepository() {

    private var currentPage = Common.REQUEST_PAGE_NO
    private var backUpPage = currentPage

    suspend fun commodity(
        isRefresh: Boolean = false,
        currentTotal: Int = 0,
        listModel: MutableLiveData<ListModel<CommodityPO>>,
        loadPageStatus: MutableLiveData<LoadPageStatus>
    ) {
        loadPageStatus.postValue(LoadPageStatus.Loading)

        //先备份一份当前页面，便于后面错误时候处理
        backUpPage = currentPage
        if (isRefresh)
            currentPage = Common.REQUEST_PAGE_NO

        val result = coroutineRequest { commodity(currentPage) }

        result.fold({ data ->
            if (data.records.isNullOrEmpty() && currentPage == Common.REQUEST_PAGE_NO) {//开始加载无数据
                loadPageStatus.postValue(LoadPageStatus.Empty)
                listModel.postValue(
                    ListModel(
                        isFinishRefresh = false,
                        isRefresh = isRefresh
                    )
                )
                return
            }
            if (currentTotal >= data.totalNum) {//最后一页  showLoading 可以用来加载dialog
                listModel.postValue(
                    ListModel(
                        isFinishRefresh = true,
                        showEnd = true,
                        isRefresh = isRefresh
                    )
                )
                return
            }

            currentPage++
            listModel.postValue(
                ListModel(
                    isFinishRefresh = true,
                    showData = data.records,
                    isRefresh = isRefresh
                )
            )
        }, { exception ->
            //还原错误场景
            currentPage = backUpPage

            if (exception is UnknownHostException || exception is ConnectException) {//加载失败 这里可以区分失败原因，设置不同页面状态
                if (currentPage == Common.REQUEST_PAGE_NO) {
                    loadPageStatus.postValue(LoadPageStatus.NoNet)
                }
            } else {
                if (currentPage == Common.REQUEST_PAGE_NO) {
                    loadPageStatus.postValue(LoadPageStatus.Fail)
                }
            }
            listModel.postValue(
                ListModel(
                    isFinishRefresh = false,
                    showError = exception.message,
                    isRefresh = isRefresh
                )
            )
        })
    }

    private suspend fun commodity(page: Int): Either<Pager<CommodityPO>, Throwable> =
        dataRepository.commodity(page).dcEither()

}