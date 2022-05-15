package com.example.common.dao

import com.example.common.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

class UserIdEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserIdEntity>(Users) {
        override val dependsOnColumns: List<Column<out Any?>>
            get() = listOf(Users.id)
    }
}