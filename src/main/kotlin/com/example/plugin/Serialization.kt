package com.example.plugin

import com.example.common.response.ErrorResults
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureSerialization() {
    install(createApplicationPlugin("SerializationHandler") {
        on(CallFailed) { call, cause ->
            if (cause is BadRequestException) {
                call.respond(status = HttpStatusCode.BadRequest, ErrorResults.InvalidArgument)
            }
        }
    })
    install(ContentNegotiation) {
//        json(Json { encodeDefaults = false })
        gson {
            setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        }
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
