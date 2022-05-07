package com.example.common.dao

import com.example.common.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)

    val username by Users.username
    val headPicUrl by Users.headPicUrl
    val createdTime by Users.createdTime
    val encryptedPassword by Users.encryptedPassword
    val phoneNum by Users.phoneNum
    val emailAddress by Users.emailAddress
}