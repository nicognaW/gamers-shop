package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CartItems : IntIdTable() {
    var user = reference("user", Users)
    var product = reference("product", Products)
    var quantity = integer("quantity")
}