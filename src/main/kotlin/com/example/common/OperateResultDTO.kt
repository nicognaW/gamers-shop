package com.example.common

enum class OperateFailCauses {
    UNKNOWN,
    RECORD_NOT_EXIST
}

data class OperateResultDTO(val success: Boolean, val message: String, val cause: OperateFailCauses? = null)