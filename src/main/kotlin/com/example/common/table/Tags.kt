package com.example.common.table

import org.jetbrains.exposed.dao.id.IntIdTable


object Tags : IntIdTable() {
    var tagName = text("tag_name")
}