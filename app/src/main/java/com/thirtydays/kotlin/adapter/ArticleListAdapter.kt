package com.thirtydays.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.seabreeze.robot.base.common.adapter.BaseListAdapter
import com.seabreeze.robot.base.common.adapter.BaseViewHolder
import com.seabreeze.robot.data.net.bean.response.Article
import com.thirtydays.kotlin.databinding.ItemArticleBinding

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/23
 * @description : TODO
 * </pre>
 */
class ArticleListAdapter : BaseListAdapter<Article>(
    itemsSame = { old, new -> old.id == new.id },
    contentsSame = { old, new -> old == new }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ArticleListViewHolder(inflater)

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleListViewHolder -> holder.bind(getItem(position))
        }
    }
}

class ArticleListViewHolder(
    inflater: LayoutInflater
) : BaseViewHolder<ItemArticleBinding>(
    binding = ItemArticleBinding.inflate(inflater)
) {
    fun bind(article: Article) {
        binding.article = article
        binding.executePendingBindings()
    }
}

class ArticleListTouchHelper constructor(
    private val onSwiped: ((Int) -> Unit)
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder.adapterPosition)
    }
}
