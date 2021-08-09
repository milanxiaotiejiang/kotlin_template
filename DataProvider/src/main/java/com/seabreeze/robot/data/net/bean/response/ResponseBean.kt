package com.seabreeze.robot.data.net.bean.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserAccessTokenData(
    var id: Int,
    var token: String,
    var url: String
)

data class UserInfoData(
    val id: Int,
    val login: String,
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("gravatar_id") val gravatarId: String,
    val url: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("followers_url") val followersUrl: String,
    @SerializedName("following_url") val followingUrl: String,
    @SerializedName("gists_url") val gistsUrl: String,
    @SerializedName("starred_url") val starredUrl: String,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @SerializedName("organizations_url") val organizationsUrl: String,
    @SerializedName("repos_url") val reposUrl: String,
    @SerializedName("events_url") val eventsUrl: String,
    @SerializedName("received_events_url") val receivedEventsUrl: String,
    val type: String,
    @SerializedName("site_admin") val siteAdmin: Boolean,
    val name: String,
    val company: String,
    val blog: String,
    val location: String,
    val email: String,
    val hireable: String,
    val bio: String,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("public_gists") val publicGists: Int,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("private_gists") val privateGists: Int,
    @SerializedName("total_private_repos") val totalPrivateRepos: Int,
    @SerializedName("owned_private_repos") val ownedPrivateRepos: Int,
    @SerializedName("disk_usage") val diskUsage: Int,
    val collaborators: Int,
    @SerializedName("two_factor_authentication") val twoFactorAuthentication: Boolean
)

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