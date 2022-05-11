package com.example.module.authentication.dao

import com.example.module.authentication.service.UserInfoDTO

interface AuthDAO {
    suspend fun getEncryptedPassword(username: String): String?
    suspend fun getUserInfo(username: String): UserInfoDTO?
}