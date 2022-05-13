package com.example.common.dao

import com.example.common.table.ProductImages
import com.example.common.table.ProductTags
import com.example.common.table.Products
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductImageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductImageEntity>(ProductImages)

    var imageUrl by ProductImages.imageUrl
}

class ProductEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEntity>(Products)

    var title by Products.title
    var status by Products.status
    var price by Products.price
    val tags by TagEntity via ProductTags
    val images by ProductImageEntity referrersOn ProductImages.product
}