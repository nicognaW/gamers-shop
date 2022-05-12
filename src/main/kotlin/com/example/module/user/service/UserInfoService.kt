package com.example.module.user.service

import com.example.common.OperateResultDTO
import com.example.common.vo.PhysicalAddress
import com.example.common.vo.UserInfo

interface UserInfoService {
    suspend fun updateUserInfo(
        targetId: Int,
        userName: String?,
        headPicUrl: String?,
        phoneNum: String?,
        emailAddress: String?
    )

    suspend fun getUserInfo(id: Int): UserInfo

    suspend fun getAddressesByPage(
        id: Int,
        pageNum: Int,
        pageSize: Int,
        order: Any? = null
    ): List<PhysicalAddress>

    suspend fun getAddressesCount(id: Int): Int
    suspend fun updateAddress(
        id: Int,
        realName: String,
        phoneNum: String,
        area: String,
        city: String,
        county: String,
        detail: String
    )

    suspend fun saveAddress(
        id: Int,
        realName: String,
        phoneNum: String,
        area: String,
        city: String,
        county: String,
        detail: String,
        recordId: Int? = null
    )

    suspend fun removeAddresses(userId: Int, ids: List<Int>): OperateResultDTO
}