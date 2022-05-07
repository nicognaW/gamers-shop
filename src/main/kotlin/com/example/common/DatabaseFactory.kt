package com.example.common


import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/h2/db"
        val database = Database.connect(jdbcURL, driverClassName)
    }

    fun initTables(tableSet: Set<Table>) {
        transaction {
            for (table in tableSet) {
                SchemaUtils.create(table)
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T?): T? = newSuspendedTransaction(Dispatchers.IO) { block() }
}