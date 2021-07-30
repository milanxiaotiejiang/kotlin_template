package com.seabreeze.robot.data

import androidx.annotation.IntDef
import com.seabreeze.robot.base.ext.foundation.Mmkv
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.net.bean.response.AccountPO

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object DataSettings {

    const val EQUALIZER_DEFAULT = 0
    const val EQUALIZER_PROFESSIONAL = 1

    const val DENOISE_FIRST = 0
    const val DENOISE_SECOND = 1
    const val DENOISE_OPEN = 2
    const val PENETRATING = 3

    private var current_account: String by Mmkv("CURRENT_ACCOUNT", "")
    var subscription_status: Boolean by Mmkv("subscription_status", false)
    var token_app: String by Mmkv("TOKEN_APP", "")

    var welcome: Boolean by Mmkv("WELCOME", false)

    var commodity_id: Int by Mmkv("COMMODITY_ID", -1)
    var last_connect_time: String by Mmkv("LAST_CONNECT_TIME", "")

    var equalizer_type: Int by Mmkv("EQUALIZER_TYPE", EQUALIZER_DEFAULT)
    var equalizer_default_type: Int by Mmkv("EQUALIZER_DEFAULT_TYPE", 0)
    var equalizer_professional_type: Int by Mmkv("EQUALIZER_PROFESSIONAL_TYPE", 0)

    var denoise_type: Int by Mmkv("DENOISE_TYPE", DENOISE_FIRST)

    fun saveAccount(account: AccountPO) {
        token_app = account.accountToken
        subscription_status = account.subscriptionStatus
        account.accountToken = ""
        account.subscriptionStatus = false
        current_account = account.gToJson()
    }

    fun clearAccount() {
        token_app = ""
        current_account = ""
        subscription_status = false
    }

    fun getAccount(): AccountPO? {
        return current_account.gToBean<AccountPO>()
    }

    @IntDef(EQUALIZER_DEFAULT, EQUALIZER_PROFESSIONAL)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class EqualizerType

    @IntDef(DENOISE_FIRST, DENOISE_SECOND, DENOISE_OPEN, PENETRATING)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class DenoiseType

}