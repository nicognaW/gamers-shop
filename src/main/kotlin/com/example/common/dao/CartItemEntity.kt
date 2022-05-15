package com.example.common.dao

import com.example.common.table.CartItems
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CartItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CartItemEntity>(CartItems)

    var user by UserIdEntity referencedOn CartItems.user
    var product by ProductEntity referencedOn CartItems.product
    var quantity by CartItems.quantity
}