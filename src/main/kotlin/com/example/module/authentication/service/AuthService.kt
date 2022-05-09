package com.example.module.authentication.service

import com.example.module.authentication.dao.AuthDAO
import com.example.plugin.AuthSession
import com.example.springify.ApplicationAware

typealias UserInfoVO = AuthSession.UserInfo

interface AuthService : ApplicationAware {
    val authDAO: AuthDAO
    suspend fun isCredentialValid(username: String, password: String): Boolean
    suspend fun getUserInfo(username: String): UserInfoVO?
}
