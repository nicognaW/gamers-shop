package com.example.module.product.service

import com.example.common.PageDataDTO
import com.example.common.vo.basic.PageDTO
import com.example.common.vo.Product
import com.example.common.vo.Tag

interface ProductService {
    suspend fun getTags(): List<Tag>
    suspend fun getTaggedProductsByPage(page: PageDTO, tags: List<Tag>): PageDataDTO<Product>
    suspend fun getProductsByPage(page: PageDTO): PageDataDTO<Product>
}