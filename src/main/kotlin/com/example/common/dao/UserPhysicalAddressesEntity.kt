package com.example.common.dao

import com.example.common.table.PhysicalAddresses
import com.example.common.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnSet


/**
 * @TODO: Fix in situ tp:
 *        DEBUG Exposed - SELECT USERS.ID FROM USERS WHERE USERS.ID = 0
 */
class UserPhysicalAddressesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserPhysicalAddressesEntity>(Users) {
        override val dependsOnTables: ColumnSet
            get() = Users
        override val dependsOnColumns: List<Column<out Any?>>
            get() = listOf(Users.id)
    }

    val pas by PhysicalAddressEntity referrersOn PhysicalAddresses.user
}
