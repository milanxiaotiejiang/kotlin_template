package com.thirtydays.kotlin.model

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/7/31
 * @description :三方登录返回的数据
 * </pre>
 */
/*
{"id":"122057319574816","first_name":"Tao","last_name":"Zhang","name":"Tao Zhang","picture":{"data":{"height":50,"is_silhouette":false,"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=122057319574816&height=50&width=50&ext=1598784390&hash=AeTONt-PaR9lvuIK","width":51}}}

{"id":"122057319574816","first_name":"Tao","last_name":"Zhang","name":"Tao Zhang","picture":{"data":{"height":50,"is_silhouette":false,"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=122057319574816&height=50&width=50&ext=1598786345&hash=AeS9zNQcmjGZhivE","width":51}}}
"birthday":"06\/01\/1992",

{"id":"122057319574816","gender":"male","name":"Tao Zhang","picture":{"data":{"height":50,"is_silhouette":false,"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=122057319574816&height=50&width=50&ext=1598786810&hash=AeTjeF-ErcNGJd_7","width":51}}}
 */
// id,age_range,birthday,email,first_name,last_name,gender,link,location,middle_name,name,apprequests,friends,permissions,picture

object ThirdInfo {
    const val FACEBOOK_MALE = "male"
}

data class FacebookInfo(
    val id: String,
    val email: String,
    val gender: String,
    val name: String,
    val picture: Picture
)

data class Picture(
    val `data`: Data
)

data class Data(
    val height: Int,
    val is_silhouette: Boolean,
    val url: String,
    val width: Int
)