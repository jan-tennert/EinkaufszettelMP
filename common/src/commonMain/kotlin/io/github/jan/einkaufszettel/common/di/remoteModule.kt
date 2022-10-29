package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.remote.*
import org.koin.dsl.module

val remoteModule = module {
    single<ProductEntryApi> { ProductEntryApiImpl(get()) }
    single<ProfileApi> { ProfileApiImpl(get()) }
    single<ShopApi> { ShopApiImpl(get()) }
    single<CardApi> { CardApiImpl(get()) }
}