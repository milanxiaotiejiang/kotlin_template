package com.thirtydays.kotlin.mvp

import com.seabreeze.robot.base.ext.execute
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Either
import com.seabreeze.robot.base.ext.foundation.dcEither
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.framework.mvp.view.BaseView
import com.seabreeze.robot.data.DataApplication
import com.seabreeze.robot.data.net.bean.response.Message
import io.reactivex.disposables.Disposable

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/6
 * @description : MessagePresenter
 * </pre>
 */
interface MessageView : BaseView<MessagePresenter> {
    fun onMessageId(either: Either<Message, Throwable>)
}

class MessagePresenter : BasePresenter<MessageView>() {

    fun messageId(messageId: String): Disposable =
        DataApplication.dataRepository.messageId(messageId)
            .map {
                if (it.resultStatus) {
                    val resultData = it.resultData
                    val messageContent = resultData.messageContent
                    if (messageContent.isNotEmpty()) {
                        // 方式1
//                        val replace = messageContent.replace("<img", "<img width=\"100%\"")
//                        resultData.messageContent = replace

                        val standard = "<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "    <meta charset=\"utf-8\" />\n" +
                                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\" />\n" +
                                "    <style>\n" +
                                "         *{ box-sizing: border-box !important; }\n" +
                                "          html,body{ width:100%; margin:0; padding:0; }\n" +
                                "          body{ padding:0 10px; }\n" +
                                "          img{ width:100% !important; height:auto !important; }\n" +
                                "    </style>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                messageContent +
                                "<script>\n" +
                                "     function setTagStyle(tagName){\n" +
                                "        var tags = document.getElementsByTagName(tagName);\n" +
                                "        for(var i=0;i<tags.length;i++){\n" +
                                "            tags[i].style.width = \"100%\";\n" +
                                "        }\n" +
                                "    }\n" +
                                "    setTagStyle(\"div\");\n" +
                                "    setTagStyle(\"p\");\n" +
                                "</script>\n" +
                                "</body>\n" +
                                "</html>"
                        resultData.messageContent = standard
                    }
                }
                return@map it
            }
            .execute(lifecycleProvider)
            .subscribe({
                mView.onMessageId(it.dcEither())
            }, {
                mView.onError(BaseThrowable.ExternalThrowable(it))
            })
}