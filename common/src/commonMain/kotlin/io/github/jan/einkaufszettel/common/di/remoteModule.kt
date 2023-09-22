package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.remote.CardApi
import io.github.jan.einkaufszettel.common.data.remote.CardApiImpl
import io.github.jan.einkaufszettel.common.data.remote.NutritionApi
import io.github.jan.einkaufszettel.common.data.remote.NutritionApiImpl
import io.github.jan.einkaufszettel.common.data.remote.ProductEntryApi
import io.github.jan.einkaufszettel.common.data.remote.ProductEntryApiImpl
import io.github.jan.einkaufszettel.common.data.remote.ProfileApi
import io.github.jan.einkaufszettel.common.data.remote.ProfileApiImpl
import io.github.jan.einkaufszettel.common.data.remote.RecipeApi
import io.github.jan.einkaufszettel.common.data.remote.RecipeApiImpl
import io.github.jan.einkaufszettel.common.data.remote.ShopApi
import io.github.jan.einkaufszettel.common.data.remote.ShopApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteModule = module {
    single { HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    } }

    single<ProductEntryApi> { ProductEntryApiImpl(get()) }
    single<ProfileApi> { ProfileApiImpl(get()) }
    single<ShopApi> { ShopApiImpl(get()) }
    single<CardApi> { CardApiImpl(get()) }
    single<NutritionApi> { NutritionApiImpl(get()) }
    single<RecipeApi> { RecipeApiImpl(get()) }
}