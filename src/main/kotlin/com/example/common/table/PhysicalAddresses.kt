package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object PhysicalAddresses : IntIdTable() {
    var user = reference("user", Users)
    var realName = text("real_name")
    var phoneNum = text("phone_num")
    var area = text("area")
    var city = text("city")
    var county = text("county")
    var detail = text("detail")
    val createdTime = datetime("created_time").default(LocalDateTime.now())
}