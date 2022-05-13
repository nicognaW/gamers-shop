package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Products : IntIdTable() {
    val title = text("title")
    val price = decimal("price", 5, 2)
    val status = text("status")
}

object ProductTags : IntIdTable() {
    val product = reference("product", Products)
    val tag = reference("tag", Tags)
}

object ProductImages : IntIdTable() {
    val product = reference("product", Products)
    val imageUrl = text("image_url")
}

