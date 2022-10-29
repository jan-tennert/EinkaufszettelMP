package io.github.jan.einkaufszettel.common.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(platformConfiguration: KoinApplication.() -> Unit = {}) = startKoin {
    platformConfiguration()
    println("init koin")
    modules(
        databaseModule,
        supabaseModule,
        remoteModule,
        localModule,
        settingsModule,
        platformModule
    )
}