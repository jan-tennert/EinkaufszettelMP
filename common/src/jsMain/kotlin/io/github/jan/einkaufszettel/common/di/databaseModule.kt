package io.github.jan.einkaufszettel.common.di

import einkaufszettel.db.CardDto
import einkaufszettel.db.ProductEntryDto
import einkaufszettel.db.ShopDto
import io.github.jan.einkaufszettel.common.data.local.EinkaufszettelDatabase
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.InstantAdapter
import io.github.jan.einkaufszettel.common.data.local.sqldelight.adapter.ListToStringAdapter
import org.koin.dsl.module

actual val databaseModule = module {
    single { EinkaufszettelDatabase(get(), CardDto.Adapter(InstantAdapter, ListToStringAdapter), ProductEntryDto.Adapter(
        InstantAdapter
    ), ShopDto.Adapter(InstantAdapter, ListToStringAdapter)) }
}