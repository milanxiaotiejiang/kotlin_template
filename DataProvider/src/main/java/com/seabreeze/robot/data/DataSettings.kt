package com.seabreeze.robot.data

import com.seabreeze.robot.base.ext.foundation.Mmkv
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.net.bean.response.UserInfo

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object DataSettings {

    var token_app: String by Mmkv("TOKEN_APP", "")

    var username: String by Mmkv("USERNAME", "")
    var password: String by Mmkv("PASSWORD", "")

    private var current_account: String by Mmkv("CURRENT_ACCOUNT", "")

    fun saveAccount(userInfo: UserInfo) {
        current_account = userInfo.gToJson()
    }

    fun localAccount(): UserInfo? {
        var info: UserInfo? = null
        if (current_account.isNotEmpty()) {
            info = current_account.gToBean<UserInfo>()
        }
        return info
    }

    fun clear() {
        current_account = ""
        password = ""
    }
}