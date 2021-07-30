package com.seabreeze.robot.third.model

import android.os.Parcelable
import com.seabreeze.robot.third.ext.Wxbody
import kotlinx.parcelize.Parcelize

/**
 * User: milan
 * Time: 2020/9/19 0:07
 * Des:
 */
data class Recharge(
    var alibody: String? = "",
    var wxbody: Wxbody? = null
)

@Parcelize
data class ThirdInfo(
    val openId: String,
    val threeType: String,
    val nickName: String,
    val headImg: String
) : Parcelable