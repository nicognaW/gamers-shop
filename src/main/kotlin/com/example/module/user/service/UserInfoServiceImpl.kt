package com.example.module.user.service

import com.example.common.DatabaseFactory
import com.example.common.dao.PhysicalAddressEntity
import com.example.common.dao.UserInfoEntity
import com.example.common.dao.UserPhysicalAddressesEntity
import com.example.common.vo.PhysicalAddress
import com.example.common.vo.UserInfo
import com.example.springify.ApplicationAware
import com.example.springify.Service
import io.ktor.server.application.*

@Service
class UserInfoServiceImpl(override val application: Application) : UserInfoService, ApplicationAware {
    override suspend fun updateUserInfo(
        targetId: Int, userName: String?, headPicUrl: String?, phoneNum: String?, emailAddress: String?
    ) = DatabaseFactory.dbQuery {
        val targetEntity = UserInfoEntity[targetId]
        userName?.let {
            targetEntity.username = it
        }
        headPicUrl?.let {
            targetEntity.headPicUrl = it
        }
        phoneNum?.let {
            targetEntity.phoneNum = it
        }
        emailAddress?.let {
            targetEntity.emailAddress = it
        }
    } ?: Unit

    override suspend fun updateAddress(
        id: Int,
        realName: String,
        phoneNum: String,
        area: String,
        city: String,
        county: String,
        detail: String
    ) {
        DatabaseFactory.dbQuery {
            val targetEntity = PhysicalAddressEntity[id]
            targetEntity.realName = realName
            targetEntity.phoneNum = phoneNum
            targetEntity.area = area
            targetEntity.city = city
            targetEntity.county = county
            targetEntity.detail = detail
        }
    }

    /**
     * @TODO: make DTOs and interfaces for paging, the count method is not efficient at the moment.
     */
    override suspend fun getAddressesByPage(
        id: Int,
        pageNum: Int,
        pageSize: Int,
        order: Any?
    ): List<PhysicalAddress> =
        DatabaseFactory.dbQuery {
            return@dbQuery UserPhysicalAddressesEntity[id].pas.copy()
                .limit(pageSize, ((pageNum - 1) * pageSize).toLong())
                .map {
                    return@map PhysicalAddress(
                        it.id.value,
                        it.realName,
                        it.phoneNum,
                        it.area,
                        it.city,
                        it.county,
                        it.detail
                    )
                }
        } ?: emptyList()

    override suspend fun getAddressesCount(id: Int): Int =
        DatabaseFactory.dbQuery {
            return@dbQuery UserPhysicalAddressesEntity[id].pas.copy().count().toInt()
        } ?: 0

    override suspend fun saveAddress(
        id: Int,
        realName: String,
        phoneNum: String,
        area: String,
        city: String,
        county: String,
        detail: String,
        recordId: Int?
    ) {
        DatabaseFactory.dbQuery {
            if (recordId == null) PhysicalAddressEntity.new {
                this.realName = realName
                this.phoneNum = phoneNum
                this.area = area
                this.city = city
                this.county = county
                this.detail = detail
                this.user = UserInfoEntity[id]
            } else updateAddress(recordId, realName, phoneNum, area, city, county, detail)
        }
    }

    override suspend fun removeAddresses(userId: Int, ids: List<Int>) {
        DatabaseFactory.dbQuery {
            ids.forEach {
                PhysicalAddressEntity[it].delete()
            }
        }
    }

    override suspend fun getUserInfo(id: Int): UserInfo = DatabaseFactory.dbQuery {
        val entity = UserInfoEntity[id]
        return@dbQuery UserInfo(
            id = entity.id.value,
            username = entity.username,
            headPicUrl = entity.headPicUrl,
            phoneNum = entity.phoneNum,
            emailAddress = entity.emailAddress
        )
    }!!
}