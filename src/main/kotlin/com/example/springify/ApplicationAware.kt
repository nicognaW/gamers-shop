package com.example.springify

import io.ktor.server.application.*

interface ApplicationAware {
    val application: Application
}