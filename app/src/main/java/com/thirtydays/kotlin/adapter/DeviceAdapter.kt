package com.thirtydays.kotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.seabreeze.robot.data.net.bean.response.CommodityPO
import com.seabreeze.robot.data.utils.image.GlideEngine
import com.thirtydays.kotlin.R

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/7/30
 * @description : ChooseDeviceAdapter
 * </pre>
 */
class DeviceAdapter : BaseQuickAdapter<CommodityPO, BaseViewHolder>(R.layout.item_device),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: CommodityPO) {
        holder.setText(R.id.tvDevice, item.commodityName)
        GlideEngine.INSTANCE.loadImage(
            context,
            item.commodityPicture,
            holder.getView(R.id.ivDevice)
        )
        holder.setVisible(R.id.ivChoose, item.choice)
    }

}