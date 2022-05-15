package com.example.common.vo.basic

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

inline fun <reified T> PipelineContext<*, ApplicationCall>.pageResponse(meta: Page, data: List<T>): Map<String, Any> =
    ordinaryResponse(meta, data)


inline fun <reified T> PipelineContext<*, ApplicationCall>.ordinaryResponse(
    meta: Any,
    data: List<T>
): Map<String, Any> = mapOf("meta" to meta, "data" to data)
