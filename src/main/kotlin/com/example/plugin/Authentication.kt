package com.example.plugin

import com.example.common.vo.UserInfo
import com.example.module.authentication.CryptoUtils
import com.example.module.authentication.controller.LoginController.Companion.authenticationInit
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import kotlin.time.Duration.Companion.days


data class AuthSession(var info: UserInfo) : Principal

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.configureAuthentication() {
    CryptoUtils.initKey(environment.config.propertyOrNull("ktor.crypto_key")?.getString())
    install(Sessions) {
        cookie<AuthSession>("auth_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAge = 7.days
            cookie.httpOnly = true
            cookie.secure = !this@configureAuthentication.developmentMode
        }
    }
    authenticationInit()
}