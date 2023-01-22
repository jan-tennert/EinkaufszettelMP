package io.github.jan.einkaufszettel.common.data.local.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase

actual class DriverFactory {

    companion object {
        private var driver: SqlDriver? = null
    }

    actual suspend fun initDriver() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        EinkaufszettelDatabase.Schema.create(driver)
        DriverFactory.driver = driver
    }

    actual fun driver(): SqlDriver {
        return driver ?: throw IllegalStateException("Driver not initialized")
    }
}