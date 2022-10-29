package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.*
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val localModule = module {
    single<RootDataSource> { RootDataSourceImpl(get()) }
    single<ProductEntryDataSource> { ProductEntryDataSourceImpl(get()) }
    single<ShopDataSource> { ShopDataSourceImpl(get()) }
    single<LocalUserDataSource> { LocalUserDataSourceImpl(get()) }
    single<CardDataSource> { CardDataSourceImpl(get()) }
}