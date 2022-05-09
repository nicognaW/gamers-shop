package com.example.module.authentication.dao

import com.example.plugin.AuthSession

typealias UserInfoDTO = AuthSession.UserInfo

interface AuthDAO {
    suspend fun getEncryptedPassword(username: String): String?
    suspend fun getUserInfo(username: String): UserInfoDTO?
}