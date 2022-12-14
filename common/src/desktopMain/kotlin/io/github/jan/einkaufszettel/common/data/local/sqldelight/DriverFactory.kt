package io.github.jan.einkaufszettel.common.data.local.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        EinkaufszettelDatabase.Schema.create(driver)
        return driver
    }
}