package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.remote.*
import io.github.jan.einkaufszettel.common.data.remote.ProductEntryApiImpl
import io.github.jan.einkaufszettel.common.data.remote.ProfileApiImpl
import org.koin.dsl.module

val remoteModule = module {
    single<ProductEntryApi> { ProductEntryApiImpl(get()) }
    single<ProfileApi> { ProfileApiImpl(get()) }
    single<ShopApi> { ShopApiImpl(get()) }
}