package com.example.common.vo


@kotlinx.serialization.Serializable
data class Product(
    val id: Int? = null,
    val title: String? = null,
    val price: Int? = null,
    val tags: List<Tag>? = null,
    val status: String? = null,
    val images: List<String>? = null
)
