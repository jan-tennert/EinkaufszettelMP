package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.CardDataSource
import io.github.jan.einkaufszettel.common.data.local.CardDataSourceImpl
import io.github.jan.einkaufszettel.common.data.local.LocalUserDataSource
import io.github.jan.einkaufszettel.common.data.local.LocalUserDataSourceImpl
import io.github.jan.einkaufszettel.common.data.local.ProductEntryDataSource
import io.github.jan.einkaufszettel.common.data.local.ProductEntryDataSourceImpl
import io.github.jan.einkaufszettel.common.data.local.RecipeDataSource
import io.github.jan.einkaufszettel.common.data.local.RecipeDataSourceImpl
import io.github.jan.einkaufszettel.common.data.local.RootDataSource
import io.github.jan.einkaufszettel.common.data.local.RootDataSourceImpl
import io.github.jan.einkaufszettel.common.data.local.ShopDataSource
import io.github.jan.einkaufszettel.common.data.local.ShopDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val localModule = module {
    single<RootDataSource> { RootDataSourceImpl(get()) }
    single<ProductEntryDataSource> { ProductEntryDataSourceImpl(get()) }
    single<ShopDataSource> { ShopDataSourceImpl(get()) }
    single<LocalUserDataSource> { LocalUserDataSourceImpl(get()) }
    single<CardDataSource> { CardDataSourceImpl(get()) }
    single<RecipeDataSource> { RecipeDataSourceImpl(get()) }
}