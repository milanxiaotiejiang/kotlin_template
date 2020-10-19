package com.thirtydays.kotlin.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.seabreeze.robot.base.ext.setGone
import com.seabreeze.robot.base.ext.setVisible
import com.seabreeze.robot.data.utils.image.GlideEngine
import com.thirtydays.kotlin.R

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/10/16
 * @description : TODO
 * </pre>
 */

class ChoseImage(
    val path: String,
    override val itemType: Int = 0,
) : MultiItemEntity {
    override fun toString(): String {
        return path
    }
}

class ChoseImageAdapter :
    BaseMultiItemQuickAdapter<ChoseImage, BaseViewHolder>() {

    init {
        addItemType(-1, R.layout.item_chose_image)
        addItemType(0, R.layout.item_chose_image)
    }

    override fun convert(holder: BaseViewHolder, item: ChoseImage) {
        val ivImage = holder.getView<ImageView>(R.id.image)
        val ivDel = holder.getView<ImageView>(R.id.ivDelete)
        if (holder.itemViewType == 0) {
            ivDel.setVisible()
            GlideEngine.INSTANCE.loadAnyImage(context, item.path, imageView = ivImage)
        } else {
            ivDel.setGone()
            ivImage.setImageResource(R.mipmap.pic_add)
        }
    }

}