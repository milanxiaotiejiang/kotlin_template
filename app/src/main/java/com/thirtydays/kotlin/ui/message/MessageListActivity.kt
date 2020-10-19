package com.thirtydays.kotlin.ui.message

import androidx.recyclerview.widget.DividerItemDecoration
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.model.Either
import com.seabreeze.robot.base.router.startMessage
import com.seabreeze.robot.base.ui.activity.BaseMvpActivity
import com.seabreeze.robot.data.net.bean.response.Message
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.adapter.MessageAdapter
import com.thirtydays.kotlin.mvp.MessageListPresenter
import com.thirtydays.kotlin.mvp.MessageListView
import kotlinx.android.synthetic.main.activity_message_list.*
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class MessageListActivity : BaseMvpActivity<MessageListPresenter>(), MessageListView {

    private lateinit var mAdapter: MessageAdapter

    override fun getLayoutId() = R.layout.activity_message_list

    override fun initData() {

        addDisposable(commonTitleBar.leftImageButton.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        mAdapter = MessageAdapter()
        recyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            val message = mAdapter.data[position]
            startMessage(message.messageTitle, message.messageId.toString(), true)
        }

        mPresenter.message()
    }

    override fun onMessage(either: Either<List<Message>, Throwable>) {
        either.fold({
            mAdapter.setList(it)
        }, {
            onError(it)
        })

    }

}
