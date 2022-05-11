package com.example.module.authentication.service

import com.example.module.authentication.dao.AuthDAO
import com.example.common.vo.UserInfo
import com.example.springify.ApplicationAware

typealias UserInfoVO = UserInfo
typealias UserInfoDTO = UserInfo

interface AuthService : ApplicationAware {
    val authDAO: AuthDAO
    suspend fun isCredentialValid(username: String, password: String): Boolean
    suspend fun getUserInfo(username: String): UserInfo?
}
