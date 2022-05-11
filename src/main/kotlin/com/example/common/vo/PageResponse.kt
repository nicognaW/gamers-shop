package com.example.common.vo

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

inline fun <reified T> PipelineContext<*, ApplicationCall>.pageResponse(meta: Page, data: List<T>): Map<String, Any> {
    return mapOf("meta" to meta, "data" to data)
}