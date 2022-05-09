package com.example.module.authentication.service

import com.example.module.authentication.CryptoUtils
import com.example.module.authentication.dao.AuthDAO
import com.example.springify.Service
import io.ktor.server.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Suppress("unused")
@Service
class AuthServiceImpl(override val application: Application) : AuthService {
    override val authDAO: AuthDAO by application.closestDI().instance()
    override suspend fun isCredentialValid(username: String, password: String): Boolean =
        authDAO.getEncryptedPassword(username) == CryptoUtils.encryptPassword(password)

    override suspend fun getUserInfo(username: String): UserInfoVO? = authDAO.getUserInfo(username)
}
