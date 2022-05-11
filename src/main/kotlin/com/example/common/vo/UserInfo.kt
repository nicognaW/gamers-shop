package com.example.common.vo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserInfo(
    @kotlinx.serialization.Transient
    val id: Int? = null,

    val username: String? = null,

    @SerialName("head_pic_url")
    @SerializedName("head_pic_url")
    val headPicUrl: String? = null,

    @SerialName("phone_num")
    @SerializedName("phone_num")
    val phoneNum: String? = null,

    @SerialName("email_address")
    @SerializedName("email_address")
    val emailAddress: String? = null,
)