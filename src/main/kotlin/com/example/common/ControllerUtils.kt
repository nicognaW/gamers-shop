package com.example.common

import com.example.common.response.ErrorResults
import com.example.common.vo.UserInfo
import com.example.plugin.AuthSession
import com.google.gson.JsonSyntaxException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.reflect.*
import kotlinx.serialization.SerializationException

suspend inline fun ApplicationCall.infoOrIdentityless(): UserInfo? {
    val info = this.sessions.get<AuthSession>()?.info
    return if (info == null) {
        this.respond(status = HttpStatusCode.Unauthorized, ErrorResults.Identityless)
        null
    } else info
}

suspend inline fun <reified T : Any> ApplicationCall.tryReceive(): T? {
    try {
        return receive(typeInfo<T>())
    } catch (e: SerializationException) {
        this.respond(status = HttpStatusCode.BadRequest, ErrorResults.InvalidArgument)
    } catch (e: JsonSyntaxException) {
        this.respond(status = HttpStatusCode.BadRequest, ErrorResults.InvalidArgument)
    } catch (e: CannotTransformContentToTypeException) {
        this.respond(status = HttpStatusCode.BadRequest, ErrorResults.InvalidArgument)
    }
    return null
}

fun Int?.ensure() = this ?: throw IllegalStateException("value is null")