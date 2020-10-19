package com.thirtydays.kotlin.mvp

import com.seabreeze.robot.base.ext.dcEither
import com.seabreeze.robot.base.ext.execute
import com.seabreeze.robot.base.model.Either
import com.seabreeze.robot.base.presenter.BasePresenter
import com.seabreeze.robot.base.presenter.view.BaseView
import com.seabreeze.robot.data.DataApplication.Companion.dataRepository
import com.seabreeze.robot.data.net.bean.response.Message
import io.reactivex.disposables.Disposable

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/3
 * @description : MessagePresenter
 * </pre>
 */
interface MessageListView : BaseView<MessageListPresenter> {
    fun onMessage(either: Either<List<Message>, Throwable>)
}

class MessageListPresenter : BasePresenter<MessageListView>() {

    fun message(): Disposable = dataRepository.message()
        .execute(lifecycleProvider)
        .subscribe({
            mView.onMessage(it.dcEither())
        }, {
            mView.onError(it)
        })
}