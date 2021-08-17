//package com.thirtydays.kotlin.adapter
//
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.viewholder.BaseViewHolder
//import com.seabreeze.robot.base.ext.tool.formatTimeYMD
//import com.seabreeze.robot.data.net.bean.response.Message
//import com.thirtydays.kotlin.R
//
///**
// * <pre>
// * @user : milanxiaotiejiang
// * @email : 765151629@qq.com
// * @version : 1.0
// * @date : 2020/8/3
// * @description : MessageAdapter
// * </pre>
// */
//class MessageAdapter : BaseQuickAdapter<Message, BaseViewHolder>(R.layout.item_message) {
//    override fun convert(holder: BaseViewHolder, item: Message) {
//        holder.setText(R.id.title, item.messageTitle)
//        holder.setText(R.id.date, item.updateTime.formatTimeYMD())
//    }
//}