package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.remote.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteModule = module {
    single { HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    } }

    single<ProductEntryApi> { ProductEntryApiImpl(get()) }
    single<ProfileApi> { ProfileApiImpl(get()) }
    single<ShopApi> { ShopApiImpl(get()) }
    single<CardApi> { CardApiImpl(get()) }
    single<NutritionApi> { NutritionApiImpl(get()) }
}