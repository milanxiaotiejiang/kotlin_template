package com.thirtydays.kotlin.mvp

import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.framework.mvp.view.BaseView

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
//    fun onMessage(either: Either<List<Message>, BaseThrowable>)
}

class MessageListPresenter : BasePresenter<MessageListView>() {

//    fun message(): Disposable = dataRepository.message()
//        .execute(lifecycleProvider)
//        .subscribe({
//            mView.onMessage(it.dcEither())
//        }, {
//            mView.onError(BaseThrowable.ExternalThrowable(it))
//        })
}