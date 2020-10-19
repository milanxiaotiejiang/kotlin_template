package com.thirtydays.kotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/15
 * @description : TODO
 * </pre>
 */
class HomeAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_expandable_list_item_1, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(android.R.id.text1, item)
    }
}