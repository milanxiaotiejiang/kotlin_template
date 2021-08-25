package com.thirtydays.kotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.seabreeze.robot.base.ext.tool.getResColor
import com.seabreeze.robot.data.net.bean.response.Article
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.databinding.ItemArticleBinding
import kotlin.random.Random

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/23
 * @description : TODO
 * </pre>
 */
class ArticleListAdapter :
    BaseQuickAdapter<Article, BaseDataBindingHolder<ItemArticleBinding>>(R.layout.item_article) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemArticleBinding>,
        item: Article,
        payloads: List<Any>
    ) {
        holder.dataBinding?.run {
            payloads.forEach { payload ->
                with(payload) {
                    if (payload is String) {
                        when (this) {
                            CHANGE_COLOR -> {
                                val nextInt = Random.nextInt(colors.size)
                                val colorInt = colors[nextInt]
                                tvTitle.setBackgroundColor(getResColor(colorInt))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemArticleBinding>, item: Article) {
        holder.dataBinding?.article = item
    }

    companion object {
        const val CHANGE_COLOR = "change_color"

        val colors = mutableListOf(
            android.R.color.holo_blue_bright,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark
        )
    }
}