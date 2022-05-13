package com.example.module.product.controller

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/products")
class ProductApi(val tags: List<String>? = null) {
    @kotlinx.serialization.Serializable
    @Resource("/tags")
    class Tags(val parent: ProductApi)

}