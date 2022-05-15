package com.example.module.cart.controller

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/cart-items")
class CartApi

@kotlinx.serialization.Serializable
@Resource("/add-to-cart")
class AddToCart