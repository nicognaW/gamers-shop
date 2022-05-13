package com.example.common.vo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Tag(
    val id: Int? = null,
    @SerialName("name")
    @SerializedName("name")
    val tagName: String? = null
)
