package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
    val username = text("username").nullable().uniqueIndex()
    val headPicUrl = text("head_pic_url").nullable()
    val createdTime = datetime("created_time").default(LocalDateTime.now())
    val encryptedPassword = text("encrypted_password")
    val phoneNum = text("phone_num").nullable()
    val emailAddress = text("email_address").nullable()
}