package com.example.module.user.controller

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/user-info")
class UserInfoApi {
    @kotlinx.serialization.Serializable
    @Resource("/update")
    class Update(val parent: UserInfoApi)

    @kotlinx.serialization.Serializable
    @Resource("/addresses")
    class Addresses(val parent: UserInfoApi)

    @kotlinx.serialization.Serializable
    @Resource("/save-address")
    class SaveAddress(val parent: UserInfoApi)

    @kotlinx.serialization.Serializable
    @Resource("/remove-addresses")
    class RemoveAddresses(val parent: UserInfoApi)

    @kotlinx.serialization.Serializable
    @Resource("/characters")
    class Characters(val parent: UserInfoApi)
}