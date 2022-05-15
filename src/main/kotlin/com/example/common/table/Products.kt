package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Products : IntIdTable() {
    val title = text("title")
    val price = decimal("price", 5, 2)
    val status = text("status")
}

object ProductTags : Table() {
    val product = reference("product", Products)
    val tag = reference("tag", Tags)

    override val primaryKey = PrimaryKey(product, tag, name = "PK_ProductTags_product_tag")
}

object ProductImages : IntIdTable() {
    val product = reference("product", Products)
    val imageUrl = text("image_url")
}

