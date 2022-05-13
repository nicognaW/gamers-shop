package com.example.module.product.service

import com.example.common.PageDataDTO
import com.example.common.vo.Page
import com.example.common.vo.Product
import com.example.common.vo.Tag

interface ProductService {
    suspend fun getTags(): List<Tag>
    suspend fun getTaggedProductsByPage(page: Page, tags: List<Tag>): PageDataDTO<Product>
    suspend fun getProductsByPage(page: Page): PageDataDTO<Product>
}