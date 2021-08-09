package com.seabreeze.robot.data.net.bean.request

import com.google.gson.annotations.SerializedName

data class EmailLoginDTO(val email: String, val password: String)

data class GithubLoginRequest(
    val scopes: List<String>,
    val note: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String
) {
    companion object {
        fun generate(note: String, clientId: String, clientSecret: String): GithubLoginRequest =
            GithubLoginRequest(
                scopes = listOf("user", "repo", "gist", "notifications"),
                note = note,
                clientId = clientId,
                clientSecret = clientSecret
            )
    }
}