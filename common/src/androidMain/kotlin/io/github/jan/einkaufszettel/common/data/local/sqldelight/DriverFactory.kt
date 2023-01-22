package io.github.jan.einkaufszettel.common.data.local.sqldelight

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase

actual class DriverFactory(private val context: Context) {

    companion object {
        private var driver: SqlDriver? = null
    }

    actual suspend fun initDriver() {
        driver = AndroidSqliteDriver(EinkaufszettelDatabase.Schema, context, "einkaufszettel.db")
    }

    actual fun driver(): SqlDriver {
        return driver ?: throw IllegalStateException("Driver not initialized")
    }


}