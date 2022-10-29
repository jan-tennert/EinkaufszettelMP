package io.github.jan.einkaufszettel.common.data.local.sqldelight

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(EinkaufszettelDatabase.Schema, context, "einkaufszettel.db")
    }
}