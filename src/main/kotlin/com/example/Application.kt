package com.example

import com.example.common.DatabaseFactory
import com.example.plugin.configureAuthentication
import com.example.plugin.configureRouting
import com.example.plugin.configureSerialization
import com.example.plugin.configureWebSockets
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.mainModule() {
    install(DefaultHeaders)
    install(StatusPages)
    install(AutoHeadResponse)
    install(Resources)

    configureAuthentication()
    configureRouting()
    configureWebSockets()
    configureSerialization()
    DatabaseFactory.init()
}
