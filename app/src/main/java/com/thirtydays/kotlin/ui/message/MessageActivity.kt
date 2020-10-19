package com.thirtydays.kotlin.ui.message

import android.content.Intent
import android.os.Build
import android.text.Html
import com.alibaba.android.arouter.facade.annotation.Route
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ext.formatTimeYMD
import com.seabreeze.robot.base.ext.getResColor
import com.seabreeze.robot.base.ext.setGone
import com.seabreeze.robot.base.model.Either
import com.seabreeze.robot.base.router.MESSAGE_CONTENT
import com.seabreeze.robot.base.router.MESSAGE_FROM
import com.seabreeze.robot.base.router.MESSAGE_TITLE
import com.seabreeze.robot.base.router.RouterPath
import com.seabreeze.robot.base.ui.web.WebMvpActivity
import com.seabreeze.robot.data.net.bean.response.Message
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.mvp.MessagePresenter
import com.thirtydays.kotlin.mvp.MessageView
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message_list.commonTitleBar
import java.util.concurrent.TimeUnit


/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/3
 * @description : 单个消息页面
 * </pre>
 */
@Route(path = RouterPath.AppCenter.PATH_APP_MESSAGE)
class MessageActivity : WebMvpActivity<MessagePresenter>(), MessageView {

//    @JvmField
//    @Autowired(name = MESSAGE_TITLE)
//    var messageTitle: String = ""
//
//    @JvmField
//    @Autowired(name = MESSAGE_CONTENT)
//    var messageContent: String = ""

    private var messageTitle: String? = null
    private var messageContent: String? = null

    private var fromList: Boolean = false

    override fun getLayoutId() = R.layout.activity_message

    override fun initData() {
        super.initData()

        progressBar.setGone()

        addDisposable(commonTitleBar.leftImageButton.clicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                onBackPressed()
            })

        parseIntent(intent)

    }

    private fun parseIntent(intent: Intent) {
        messageTitle = intent.getStringExtra(MESSAGE_TITLE)
        tvSubTitle.text = messageTitle

        messageContent = intent.getStringExtra(MESSAGE_CONTENT)
        messageContent?.let {
            mPresenter.messageId(it)
        }

        fromList = intent.getBooleanExtra(MESSAGE_FROM, false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { parseIntent(it) }
    }

    override fun onMessageId(either: Either<Message, Throwable>) {
        either.fold({
            val body = it.messageContent

            tvTime.text = it.updateTime.formatTimeYMD()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.text = Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT)
            } else {
                tvContent.text = Html.fromHtml(body)
            }

            webView.loadData(body, "text/html; charset=utf-8", Charsets.UTF_8.toString())
            webView.setBackgroundColor(getResColor(R.color.colorBg))
        }, {
            onError(it)
        })

    }

}