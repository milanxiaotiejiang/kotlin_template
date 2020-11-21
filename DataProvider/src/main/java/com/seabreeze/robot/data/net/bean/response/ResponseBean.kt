package com.seabreeze.robot.data.net.bean.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountPO(
    val accountId: Int,
    val accountStatus: String,
    var accountToken: String,
    val accountType: String,
    val adminStatus: Boolean,
    val avatar: String,
    val createTime: String,
    val deletedState: Boolean,
    val email: String,
    val nickname: String,
    val password: String,
    var subscriptionStatus: Boolean,
    val updateTime: String
) : Parcelable

enum class AccountStatus(val code: String, val desc: String) {
    //未激活
    INACTIVATED("INACTIVATED", "未激活"),

    //已启用
    ENABLED("ENABLED", "已启用"),

    //已禁用
    DISABLED("DISABLED", "DISABLED");
}

enum class Gender {
    //男
    MALE,

    //女
    FEMALE
}

enum class AccountType {
    EMAIL, APPLE, TWITTER, FACEBOOK
}

data class Message(
    val createTime: String,
    val deletedState: Boolean,
    var messageContent: String,
    val messageId: Int,
    val messageStatus: String,
    val messageTitle: String,
    val updateTime: String
)

data class Pager<T>(
    val pageNo: Int,
    val records: List<T>,
    val totalNum: Int
)

@Parcelize
data class CommodityPO(
    val amazonUrl: String,
    val commodityId: Int,
    val commodityName: String,
    val commodityPicture: String,
    val createTime: String,
    val descPicture: String,
    val jdUrl: String,
    var choice: Boolean
) : Parcelable