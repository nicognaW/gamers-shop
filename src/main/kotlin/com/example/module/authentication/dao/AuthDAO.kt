package com.example.module.authentication.dao

import com.example.common.DatabaseFactory.dbQuery
import com.example.common.table.Users
import com.example.plugin.AuthSession
import com.example.springify.Repository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

typealias UserInfoDTO = AuthSession.UserInfo

interface AuthDAO {
    suspend fun getEncryptedPassword(username: String): String?
    suspend fun getUserInfo(username: String): UserInfoDTO?
}

@Suppress("unused")
@Repository
class AuthDAOImpl : AuthDAO {
    override suspend fun getEncryptedPassword(username: String): String? = dbQuery {
        Users.slice(Users.encryptedPassword).select { Users.username eq username }.map { resultRow: ResultRow ->
            return@map resultRow[Users.encryptedPassword]
        }.singleOrNull()
    }

    override suspend fun getUserInfo(username: String): UserInfoDTO? = dbQuery {
        Users.slice(Users.id, Users.username, Users.headPicUrl, Users.phoneNum, Users.emailAddress)
            .select { Users.username eq username }
            .map {
                UserInfoDTO(
                    id = it[Users.id].value,
                    username = username,
                    headPicUrl = it[Users.headPicUrl],
                    phoneNum = it[Users.phoneNum],
                    emailAddress = it[Users.emailAddress]
                )
            }
            .singleOrNull()
    }
}