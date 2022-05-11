package com.example.common.dao

import com.example.common.table.PhysicalAddresses
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PhysicalAddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PhysicalAddressEntity>(PhysicalAddresses)

    var user by UserInfoEntity referencedOn PhysicalAddresses.user

    var realName by PhysicalAddresses.realName
    var phoneNum by PhysicalAddresses.phoneNum
    var area by PhysicalAddresses.area
    var city by PhysicalAddresses.city
    var county by PhysicalAddresses.county
    var detail by PhysicalAddresses.detail
    val createdTime by PhysicalAddresses.createdTime
}