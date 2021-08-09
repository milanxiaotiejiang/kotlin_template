package com.thirtydays.kotlin.ktx

import com.seabreeze.robot.base.ext.foundation.Mmkv

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object DataSettings {
    var username by Mmkv("username", "")
    var password by Mmkv("password", "")

    var user_id: Int by Mmkv("user_id", -1)
    var name by Mmkv("name", "")
    var avatar_url by Mmkv("avatar_url", "")
}