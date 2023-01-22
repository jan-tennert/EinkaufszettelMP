package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.*
import org.koin.dsl.module

val localModule = module {
    single<RootDataSource> { RootDataSourceImpl(get()) }
    single<ProductEntryDataSource> { ProductEntryDataSourceImpl(get()) }
    single<ShopDataSource> { ShopDataSourceImpl(get()) }
    single<LocalUserDataSource> { LocalUserDataSourceImpl(get()) }
    single<CardDataSource> { CardDataSourceImpl(get()) }
}