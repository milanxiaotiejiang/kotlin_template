package com.seabreeze.robot.data.net.bean.request

data class UserLoginRequest(
    val username: String,
    val password: String
) {
    companion object {
        fun generate(username: String, password: String): UserLoginRequest =
            UserLoginRequest(username = username, password = password)
    }
}