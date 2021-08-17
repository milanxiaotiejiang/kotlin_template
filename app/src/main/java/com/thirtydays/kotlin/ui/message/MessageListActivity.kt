package com.thirtydays.kotlin.ui.message

import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.thirtydays.kotlin.databinding.ActivityMessageListBinding
import com.thirtydays.kotlin.mvp.MessageListPresenter
import com.thirtydays.kotlin.mvp.MessageListView
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class MessageListActivity : BaseMvpActivity<MessageListPresenter, ActivityMessageListBinding>(),
    MessageListView {

//    private lateinit var mAdapter: MessageAdapter

    override fun initData() {

        addDisposable(mViewBinding.commonTitleBar.leftImageButton.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })

        mViewBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

//        mAdapter = MessageAdapter()
//        mViewBinding.recyclerView.adapter = mAdapter
//
//        mAdapter.setOnItemClickListener { _, _, position ->
//            val message = mAdapter.data[position]
//            startMessage(message.messageTitle, message.messageId.toString(), true)
//        }
//
//        mPresenter.message()
    }

//    override fun onMessage(either: Either<List<Message>, BaseThrowable>) {
//        either.fold({
//            mAdapter.setList(it)
//        }, {
//            onError(it)
//        })
//    }

}
