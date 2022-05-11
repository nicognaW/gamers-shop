package com.example.common.vo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class PhysicalAddress(
    @kotlinx.serialization.Serializable
    var id: Int? = null,
    @kotlinx.serialization.Serializable
    @SerialName("real_name")
    @SerializedName("real_name")
    var realName: String,
    @kotlinx.serialization.Serializable
    @SerialName("phone_num")
    @SerializedName("phone_num")
    var phoneNum: String,
    @kotlinx.serialization.Serializable
    var area: String,
    @kotlinx.serialization.Serializable
    var city: String,
    @kotlinx.serialization.Serializable
    var county: String,
    @kotlinx.serialization.Serializable
    var detail: String
)