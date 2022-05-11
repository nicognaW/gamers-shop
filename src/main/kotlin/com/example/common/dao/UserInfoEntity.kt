package com.example.common.dao

import com.example.common.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column


/**
 * @TODO：refactor authDao using the dependsOnColumns schema.
 */
class UserInfoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserInfoEntity>(Users) {
        override val dependsOnColumns: List<Column<out Any?>>
            get() = super.dependsOnColumns.minus(Users.encryptedPassword)
    }

    var username by Users.username
    var headPicUrl by Users.headPicUrl
    var createdTime by Users.createdTime
    var phoneNum by Users.phoneNum
    var emailAddress by Users.emailAddress
}

