package io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter

import com.squareup.sqldelight.ColumnAdapter

object ListToStringAdapter : ColumnAdapter<List<String>, String> {

    override fun decode(databaseValue: String): List<String> {
        return databaseValue.split(",")
    }

    override fun encode(value: List<String>): String {
        return value.joinToString(",")
    }

}