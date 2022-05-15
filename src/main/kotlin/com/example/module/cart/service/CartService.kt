package com.example.module.cart.service

import com.example.common.OperateResultDTO
import com.example.common.PageDataDTO
import com.example.common.vo.CartItem
import com.example.common.vo.basic.PageDTO

interface CartService {
    suspend fun getCartItemsByPage(userId: Int, page: PageDTO): PageDataDTO<CartItem>
    suspend fun addToCart(userId: Int, productId: Int, quantity: Int): OperateResultDTO
    suspend fun removeCartItems(userId: Int, ids: List<Int>): OperateResultDTO
    suspend fun editCartItem(userId: Int, id: Int, quantity: Int): OperateResultDTO
}
