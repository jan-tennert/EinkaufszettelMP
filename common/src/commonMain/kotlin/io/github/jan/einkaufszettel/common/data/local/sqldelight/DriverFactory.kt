package io.github.jan.einkaufszettel.common.data.local.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import einkaufszettel.db.ProductEntryDto
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.InstantAdapter
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.ListToStringAdapter

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): EinkaufszettelDatabase {
    return EinkaufszettelDatabase(driverFactory.createDriver(), ProductEntryDto.Adapter(InstantAdapter), ShopDto.Adapter(InstantAdapter, ListToStringAdapter))
}