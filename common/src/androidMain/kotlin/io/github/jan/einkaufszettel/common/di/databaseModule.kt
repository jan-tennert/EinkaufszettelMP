package io.github.jan.einkaufszettel.common.di

import io.github.jan.einkaufszettel.common.data.local.sqldelight.DriverFactory
import io.github.jan.einkaufszettel.common.data.local.sqldelight.createDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {
    single { createDatabase(DriverFactory(androidContext())) }
}