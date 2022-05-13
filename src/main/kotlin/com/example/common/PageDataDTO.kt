package com.example.common

data class PageDataDTO<T>(
    val data: List<T>,
    val total: Int,
)