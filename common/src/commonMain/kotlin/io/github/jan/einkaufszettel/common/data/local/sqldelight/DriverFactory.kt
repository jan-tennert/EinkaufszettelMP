package io.github.jan.einkaufszettel.common.data.local.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import einkaufszettel.db.CardDto
import einkaufszettel.db.ProductEntryDto
import einkaufszettel.db.RecipeDto
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.InstantAdapter
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.ListToStringAdapter

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): EinkaufszettelDatabase {
    return EinkaufszettelDatabase(driverFactory.createDriver(), CardDto.Adapter(InstantAdapter, ListToStringAdapter), ProductEntryDto.Adapter(InstantAdapter), RecipeDto.Adapter(InstantAdapter, ListToStringAdapter), ShopDto.Adapter(InstantAdapter, ListToStringAdapter))
}